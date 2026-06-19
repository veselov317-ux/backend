package com.backend.sys.dto.request;

import com.backend.sys.entity.TicketStatus;

public record TicketUpdateRequest(
        TicketStatus status,
        Long assignedAgentId
) {
}
