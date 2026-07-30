package africa.civicbridge.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Server-side enforcement of the role table in ARCHITECTURE.md section 2.
 * Client-side permissionsFor() checks are UX only; this is what actually
 * rejects unauthorized writes, regardless of what the caller claims.
 */
@Component
public class RoleAuthorizationFilter extends OncePerRequestFilter {

    private record Rule(String method, String pathPrefix, Set<String> allowedRoles) {
        boolean matches(String method, String uri) {
            return this.method.equals(method) && uri.startsWith(pathPrefix);
        }
    }

    private final List<Rule> rules = List.of(
            new Rule("POST", "/api/content", Set.of("Admin")),
            new Rule("PUT", "/api/content/", Set.of("Admin")),
            new Rule("DELETE", "/api/content/", Set.of("Admin")),
            new Rule("POST", "/api/tracker", Set.of("Admin")),
            new Rule("PUT", "/api/tracker/", Set.of("Admin")),
            new Rule("DELETE", "/api/tracker/", Set.of("Admin")),
            new Rule("DELETE", "/api/forum/", Set.of("Moderator", "Admin")),
            new Rule("POST", "/api/forum", Set.of("Youth User", "Moderator", "Admin")),
            new Rule("POST", "/api/quizzes/", Set.of("Youth User", "Moderator", "Admin"))
    );

    private final JwtUtil jwtUtil;

    public RoleAuthorizationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        Rule matched = rules.stream()
                .filter(r -> r.matches(request.getMethod(), request.getRequestURI()))
                .findFirst()
                .orElse(null);

        if (matched == null) {
            chain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            reject(response, 401, "Missing or invalid Authorization header");
            return;
        }

        try {
            Claims claims = jwtUtil.parse(header.substring(7));
            String role = claims.get("role", String.class);
            if (role == null || !matched.allowedRoles().contains(role)) {
                reject(response, 403, "Your role does not permit this operation");
                return;
            }
        } catch (JwtException | IllegalArgumentException e) {
            reject(response, 401, "Invalid or expired token");
            return;
        }

        chain.doFilter(request, response);
    }

    private void reject(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"" + message.replace("\"", "'") + "\"}");
    }
}
