package africa.civicbridge.api.controller;

import africa.civicbridge.api.entity.AppUser;
import africa.civicbridge.api.repository.AppUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AppUserRepository users;

    public AuthController(AppUserRepository users) {
        this.users = users;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> body) {
        String name = body.get("name");
        String email = body.get("email");
        String password = body.get("password");

        if (email == null || password == null || password.length() < 6) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid name, email, or password (min 6 chars)"));
        }
        if (users.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Email already registered"));
        }

        AppUser user = new AppUser(name, email, password, "Youth User");
        users.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(safe(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        return users.findByEmail(email)
                .filter(u -> u.getPassword().equals(password))
                .<ResponseEntity<?>>map(u -> ResponseEntity.ok(safe(u)))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials")));
    }

    private Map<String, Object> safe(AppUser u) {
        return Map.of("id", u.getId(), "name", u.getName(), "email", u.getEmail(), "role", u.getRole());
    }
}
