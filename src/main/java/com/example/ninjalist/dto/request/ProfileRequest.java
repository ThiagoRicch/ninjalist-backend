package com.example.ninjalist.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record ProfileRequest(@NotEmpty(message = "username obrigatório") String name,
                             @NotEmpty(message = "email oobrigatório") String email) {
}
