package africa.civicbridge.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AuthResponse(
        @Schema(description = "Copy this value and use it as 'Bearer <token>' — either in the Swagger UI Authorize button, or as the Authorization header on your own requests.",
                example = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiIxIiwiZW1haWwiOiJhZG1pbkBjaXZpY2JyaWRnZS5hZnJpY2EiLCJyb2xlIjoiQWRtaW4ifQ.signature")
        String token,
        @Schema(example = "1") Long id,
        @Schema(example = "Amina Okafor") String name,
        @Schema(example = "admin@civicbridge.africa") String email,
        @Schema(example = "Admin") String role
) {}
