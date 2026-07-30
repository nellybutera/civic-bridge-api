package africa.civicbridge.api.controller;

import africa.civicbridge.api.dto.AuthResponse;
import africa.civicbridge.api.dto.LoginRequest;
import africa.civicbridge.api.dto.SignupRequest;
import africa.civicbridge.api.entity.AppUser;
import africa.civicbridge.api.repository.AppUserRepository;
import africa.civicbridge.api.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Signup and login. Both issue a JWT — pass it as 'Bearer <token>' via the Authorize button above to call protected endpoints.")
public class AuthController {

    private final AppUserRepository users;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(AppUserRepository users, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signup")
    @Operation(
            summary = "Create a Youth User account and get a token",
            description = "Always creates the 'Youth User' role. Returns a JWT you can use immediately — no separate login call needed."
    )
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest req) {
        if (users.existsByEmail(req.email())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Email already registered"));
        }

        AppUser user = new AppUser(req.name(), req.email(), passwordEncoder.encode(req.password()), "Youth User");
        try {
            users.save(user);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Email already registered"));
        }

        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponse(token, user.getId(), user.getName(), user.getEmail(), user.getRole()));
    }

    @PostMapping("/login")
    @Operation(
            summary = "Log in and get a token",
            description = "Use a seeded demo account (see this page's top description) or one created via /signup. "
                    + "Copy the returned 'token' into the Authorize button above to unlock 🔒 endpoints."
    )
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        return users.findByEmail(req.email())
                .filter(u -> passwordEncoder.matches(req.password(), u.getPassword()))
                .<ResponseEntity<?>>map(u -> ResponseEntity.ok(new AuthResponse(
                        jwtUtil.generateToken(u.getId(), u.getEmail(), u.getRole()),
                        u.getId(), u.getName(), u.getEmail(), u.getRole())))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials")));
    }
}
