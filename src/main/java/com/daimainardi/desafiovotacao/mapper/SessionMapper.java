package com.daimainardi.desafiovotacao.mapper;

import com.daimainardi.desafiovotacao.entity.SessionEntity;
import com.daimainardi.desafiovotacao.request.SessionRequestDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SessionMapper {

    public static SessionEntity mapRequestToEntity(SessionRequestDTO sessionRequestDTO) {
        return SessionEntity.builder()
                .agendaId(sessionRequestDTO.agendaId())
                .startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now().plusMinutes(sessionRequestDTO.durationMinutes()))
                .build();
    }
}
