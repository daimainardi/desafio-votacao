package com.daimainardi.desafiovotacao.entity;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "vote")
@Builder
public record VoteEntity(
        @Id
        String id,
        @Indexed(unique = true)
        String associateCpf,
        String sessionId,
        String value) {
}
