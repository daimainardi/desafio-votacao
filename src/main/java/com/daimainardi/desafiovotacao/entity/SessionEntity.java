package com.daimainardi.desafiovotacao.entity;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "session")
@Builder
public record SessionEntity(
        @Id
        String id,
        String agendaId,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime) {
}
