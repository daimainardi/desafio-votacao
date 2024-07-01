package com.daimainardi.desafiovotacao.response;


public record SessionResponseDTO(
        String id,
        String agendaTitle,
        Integer durationMinutes) {
}
