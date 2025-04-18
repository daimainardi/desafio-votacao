package com.daimainardi.desafiovotacao.exception;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class VotingChallengeExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateKeyException.class)
    public ErrorModel handleDuplicateKeyException(DuplicateKeyException e) {
        return ErrorModel.builder()
                .message("Duplicated fields")
                .error(e.getClass().getName())
                .status(HttpStatus.BAD_REQUEST)
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AgendaNotFoundException.class)
    public ErrorModel exceptionValidatorAgenda(AgendaNotFoundException e) {
        return ErrorModel.builder()
                .message(e.getMessage())
                .error(e.getClass().getName())
                .status(e.getStatus())
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(SessionNotFoundException.class)
    public ErrorModel exceptionValidatorSession(SessionNotFoundException e) {
        return ErrorModel.builder()
                .message(e.getMessage())
                .error(e.getClass().getName())
                .status(e.getStatus())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SessionNotActiveException.class)
    public ErrorModel exceptionValidatorSessionActive(SessionNotActiveException e) {
        return ErrorModel.builder()
                .message(e.getMessage())
                .error(e.getClass().getName())
                .status(e.getStatus())
                .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorModel handleException(Exception e) {
        return ErrorModel.builder()
                .message("Unexpected Error")
                .error(e.getClass().getName())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorModel handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ErrorModel.builder()
                .message("Invalid form")
                .error(e.getClass().getName())
                .status(HttpStatus.BAD_REQUEST)
                .fieldErrors(errors)
                .build();
    }

}
