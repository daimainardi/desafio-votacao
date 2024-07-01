package com.daimainardi.desafiovotacao.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

public record VoteRequestDTO(
        @NotBlank
        @CPF
        String associateCpf,
        @NotBlank
        String sessionId,
        @NotNull
        String value) {
}
