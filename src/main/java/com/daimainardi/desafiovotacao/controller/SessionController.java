package com.daimainardi.desafiovotacao.controller;

import com.daimainardi.desafiovotacao.request.SessionRequestDTO;
import com.daimainardi.desafiovotacao.request.VoteRequestDTO;
import com.daimainardi.desafiovotacao.response.SessionResponseDTO;
import com.daimainardi.desafiovotacao.response.VoteResultDTO;
import com.daimainardi.desafiovotacao.service.SessionService;
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
@RequestMapping(value = "/sessions")
@RequiredArgsConstructor
@Tag(name = "Session", description = "Session management")
public class SessionController {

    private final SessionService sessionService;

    @ResponseStatus(value = HttpStatus.CREATED)
    @Operation(summary = "Create a new Session")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Session successfully registered",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SessionResponseDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Bad Server")
    })
    @PostMapping
    public SessionResponseDTO create(@RequestBody @Valid SessionRequestDTO sessionRequestDTO) {
        return sessionService.createSession(sessionRequestDTO);
    }

    @Operation(summary = "Find a list of active sessions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sessions found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SessionResponseDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Sessions not found"),
            @ApiResponse(responseCode = "500", description = "Bad server")
    })
    @GetMapping
    public List<SessionResponseDTO> findAllActive() {
        return sessionService.findAllActiveSessions();
    }

    @Operation(summary = "Find the voting results by session")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Results found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = VoteResultDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Results not found"),
            @ApiResponse(responseCode = "500", description = "Bad server")
    })
    @GetMapping("{sessionId}/results")
    public VoteResultDTO getFinalVotingResult(@PathVariable String sessionId) {
        return sessionService.finalVotingResult(sessionId);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @Operation(summary = "Create a new Vote")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vote successfully registered"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Bad Server")
    })
    @PostMapping("/votes")
    public void create(@RequestBody @Valid VoteRequestDTO voteRequestDTO) {
        sessionService.createVote(voteRequestDTO);
    }
}
