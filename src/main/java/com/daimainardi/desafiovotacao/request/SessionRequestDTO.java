package com.daimainardi.desafiovotacao.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder(toBuilder = true)
public record SessionRequestDTO(
        @NotBlank
        String agendaId,
        Integer durationMinutes) {
}
