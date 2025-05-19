package com.daimainardi.desafiovotacao.response;

import com.daimainardi.desafiovotacao.enumeration.SessionStatus;

public record PayloadDTO(
        String id,
        String agendaTitle,
        Integer durationMinutes,
        SessionStatus sessionStatus) {
}
