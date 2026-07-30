package africa.civicbridge.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be a valid address")
        @Schema(example = "admin@civicbridge.africa", description = "Try one of the seeded demo accounts — see README for the full list.")
        String email,

        @NotBlank(message = "Password is required")
        @Schema(example = "admin123")
        String password
) {}
