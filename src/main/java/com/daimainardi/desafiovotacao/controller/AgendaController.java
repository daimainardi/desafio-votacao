package com.daimainardi.desafiovotacao.controller;

import com.daimainardi.desafiovotacao.request.AgendaRequestDTO;
import com.daimainardi.desafiovotacao.response.AgendaResponseDTO;
import com.daimainardi.desafiovotacao.service.AgendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/agendas")
@RequiredArgsConstructor
@Tag(name = "Agenda", description = "Agenda management")
public class AgendaController {

    private final AgendaService agendaService;

    @ResponseStatus(value = HttpStatus.CREATED)
    @Operation(summary = "Create a new Agenda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Agenda successfully registered",
            content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = AgendaResponseDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Bad Server")
    })
    @PostMapping
    public AgendaResponseDTO create(@RequestBody @Valid AgendaRequestDTO agendaRequestDTO) {
        return agendaService.createAgenda(agendaRequestDTO);
    }

    @Operation(summary = "Find a list of agendas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendas found",
            content = {@Content(mediaType = "Application/json",
            schema = @Schema(implementation = AgendaResponseDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Agendas not found"),
            @ApiResponse(responseCode = "500", description = "Bad server")
    })
    @GetMapping
    public List<AgendaResponseDTO> findAll(){
        return agendaService.findAll();
    }
}
