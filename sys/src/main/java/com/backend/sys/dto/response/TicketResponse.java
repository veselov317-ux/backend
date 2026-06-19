package com.backend.sys.dto.response;

import com.backend.sys.entity.TicketStatus;
import java.time.Instant;
import java.util.List;

public record TicketResponse(
        Long id,
        String title,
        String description,
        TicketStatus status,
        CategoryResponse category,
        UserResponse requester,
        UserResponse assignedAgent,
        List<CommentResponse> comments,
        Instant createdAt,
        Instant updatedAt
) {
}
