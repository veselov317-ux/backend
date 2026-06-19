package com.backend.sys.dto.response;

import java.time.Instant;

public record CommentResponse(
        Long id,
        String message,
        UserResponse author,
        Instant createdAt
) {
}
