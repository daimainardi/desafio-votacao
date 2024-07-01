package com.daimainardi.desafiovotacao.request;

import jakarta.validation.constraints.NotBlank;

public record AgendaRequestDTO(
        @NotBlank
        String title,
        @NotBlank
        String description) {
}
