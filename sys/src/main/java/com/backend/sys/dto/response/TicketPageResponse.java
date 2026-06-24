package com.backend.sys.dto.response;

import java.util.List;

public record TicketPageResponse(
        List<TicketListResponse> items,
        long totalElements,
        int totalPages,
        int page,
        int size
) {
}

