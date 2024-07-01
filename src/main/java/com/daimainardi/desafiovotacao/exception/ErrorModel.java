package com.daimainardi.desafiovotacao.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Builder
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorModel {
    private String message;
    private String error;
    HttpStatus status;
    Map<String, String> fieldErrors;

}
