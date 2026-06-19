package com.backend.sys.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TicketCreateRequest(
        @NotBlank @Size(max = 160) String title,
        @NotBlank @Size(max = 5000) String description,
        @NotNull Long categoryId
) {
}
