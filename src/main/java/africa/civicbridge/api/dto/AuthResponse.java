package africa.civicbridge.api.dto;

public record AuthResponse(String token, Long id, String name, String email, String role) {}
