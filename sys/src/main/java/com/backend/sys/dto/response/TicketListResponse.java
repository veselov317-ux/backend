package com.backend.sys.dto.response;

import com.backend.sys.entity.TicketStatus;
import java.time.Instant;

public record TicketListResponse(
        Long id,
        String title,
        String description,
        TicketStatus status,
        CategoryResponse category,
        UserResponse requester,
        UserResponse assignedAgent,
        Instant createdAt,
        Instant updatedAt,
        Instant closedAt
) {
}

