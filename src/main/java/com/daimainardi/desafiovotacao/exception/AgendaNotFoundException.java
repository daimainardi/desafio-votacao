package com.daimainardi.desafiovotacao.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class AgendaNotFoundException extends RuntimeException {

    private final HttpStatus status;

    public AgendaNotFoundException(String mensagem, HttpStatus status) {
        super(mensagem);
        this.status = status;
    }
}
