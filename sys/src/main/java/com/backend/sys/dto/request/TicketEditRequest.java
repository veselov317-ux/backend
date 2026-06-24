package com.backend.sys.dto.request;

public record TicketEditRequest(
        String title,
        String description,
        Long categoryId
) {
}

