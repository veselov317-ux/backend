package com.backend.sys.dto.response;

public record AuthResponse(
        String token,
        String tokenType,
        UserResponse user
) {
}
