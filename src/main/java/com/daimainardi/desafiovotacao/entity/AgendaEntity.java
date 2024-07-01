package com.daimainardi.desafiovotacao.entity;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "agenda")
@Builder
public record AgendaEntity(
        @Id
        String id,
        String title,
        String description,
        LocalDateTime creationDate) {
}
