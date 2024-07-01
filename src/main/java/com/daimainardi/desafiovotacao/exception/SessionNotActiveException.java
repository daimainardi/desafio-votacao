package com.daimainardi.desafiovotacao.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SessionNotActiveException extends RuntimeException {
    private final HttpStatus status;

    public SessionNotActiveException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
