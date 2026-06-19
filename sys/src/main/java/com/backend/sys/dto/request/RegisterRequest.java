package com.backend.sys.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank @Size(max = 120) String fullName,
        @Email @NotBlank @Size(max = 160) String email,
        @NotBlank @Size(min = 6, max = 80) String password
) {
}
