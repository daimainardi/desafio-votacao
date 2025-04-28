package com.daimainardi.desafiovotacao.controller;

import com.daimainardi.desafiovotacao.entity.SessionEntity;
import com.daimainardi.desafiovotacao.entity.VoteEntity;
import com.daimainardi.desafiovotacao.repository.SessionRepository;
import com.daimainardi.desafiovotacao.repository.VoteRepository;
import com.daimainardi.desafiovotacao.request.SessionRequestDTO;
import com.daimainardi.desafiovotacao.request.VoteRequestDTO;
import com.daimainardi.desafiovotacao.stub.StubBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class SessionControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private VoteRepository voteRepository;

    @BeforeEach
    void setUp() {
        sessionRepository.deleteAll();
        voteRepository.deleteAll();
        sessionRepository.save(StubBuilder.sessionEntity());
    }

    @Test
    @DisplayName("Deve salvar uma nova sessão")
    void shouldCreateNewSession() throws Exception {
        SessionRequestDTO sessionRequestDTO = new SessionRequestDTO("123456", 30);
        mockMvc.perform(post("/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(sessionRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.agendaTitle").value("Aumento de salário"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.durationMinutes").value(30));
    }

    @Test
    @DisplayName("Deve buscar as sessões ativas")
    void shouldSearchForActiveSessions() throws Exception {
        mockMvc.perform(get("/sessions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].agendaTitle").value("Aumento de salário"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].durationMinutes").value(30));
    }

    @Test
    @DisplayName("Deve criar um novo voto")
    void shouldRegisterTheVote() throws Exception {
        VoteRequestDTO voteRequestDTO = new VoteRequestDTO("84488737005", "78910", "SIM");
        mockMvc.perform(post("/sessions/votes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(voteRequestDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Não deve salvar novo voto, deve retornar uma SessionNotFoundException quando não encontrar a sessão")
    void shouldReturnSessionNotFoundException() throws Exception {
        VoteRequestDTO voteRequestDTO = new VoteRequestDTO("84488737005", "123456", "SIM");
        mockMvc.perform(post("/sessions/votes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(voteRequestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Não deve salvar novo voto, deve retornar uma SessionNotActiveException quando a sessão não estiver ativa")
    void shouldReturnSessionNotActiveException() throws Exception {
        SessionEntity sessionEntity = sessionRepository.save(new SessionEntity("123456","123456",
                LocalDateTime.now().minusMinutes(30), LocalDateTime.now().minusMinutes(20)));
        VoteRequestDTO voteRequestDTO = new VoteRequestDTO("84488737005", sessionEntity.id(), "SIM");
        mockMvc.perform(post("/sessions/votes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(voteRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar BAD_REQUEST, cpf inválido")
    void shouldReturnMethodArgumentNotValidException() throws Exception {
        VoteRequestDTO invalidRequest = new VoteRequestDTO("844887370", "78910", "SIM");
        mockMvc.perform(post("/sessions/votes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar o resultado da votação")
    void shouldReturnTheVotingResult() throws Exception {
        VoteEntity yes = new VoteEntity("12", "00762345098", "78910", "SIM");
        VoteEntity no = new VoteEntity("13", "05553232694", "78910", "NAO");
        voteRepository.save(yes);
        voteRepository.save(no);
        mockMvc.perform(get("/sessions/78910/results")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.sessionId").value("78910"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.countVotesYes").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.countVotesNo").value(1));
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando não houver sessões ativas")
    void shouldReturnAnEmptyListWhenThereAreNoActiveSessions() throws Exception {
        sessionRepository.deleteAll();
        SessionEntity sessionEntity = new SessionEntity("123","123456" , LocalDateTime.now().minusMinutes(30),
                LocalDateTime.now().minusMinutes(20));
        sessionRepository.save(sessionEntity);
        mockMvc.perform(get("/sessions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("Deve retornar uma AgendaNotFoundException quando tentar cadastrar uma sessão com uma agenda inexistente")
    void shouldReturnAnAgendaNotFoundException() throws Exception {
        SessionRequestDTO sessionRequestDTO = new SessionRequestDTO("12", 30);
        mockMvc.perform(post("/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(sessionRequestDTO)))
                .andExpect(status().isNotFound());
    }
}
