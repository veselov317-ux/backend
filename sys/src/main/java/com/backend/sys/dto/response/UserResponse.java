package com.backend.sys.dto.response;

import com.backend.sys.entity.Role;
import java.time.Instant;

public record UserResponse(
        Long id,
        String fullName,
        String email,
        Role role,
        boolean enabled,
        Instant createdAt
) {
}
