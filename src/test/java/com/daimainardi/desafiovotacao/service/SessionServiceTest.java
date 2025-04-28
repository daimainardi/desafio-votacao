package com.daimainardi.desafiovotacao.service;

import com.daimainardi.desafiovotacao.entity.SessionEntity;
import com.daimainardi.desafiovotacao.entity.VoteEntity;
import com.daimainardi.desafiovotacao.exception.SessionNotActiveException;
import com.daimainardi.desafiovotacao.exception.SessionNotFoundException;
import com.daimainardi.desafiovotacao.mapper.SessionMapper;
import com.daimainardi.desafiovotacao.mapper.VoteMapper;
import com.daimainardi.desafiovotacao.repository.SessionRepository;
import com.daimainardi.desafiovotacao.repository.VoteRepository;
import com.daimainardi.desafiovotacao.request.SessionRequestDTO;
import com.daimainardi.desafiovotacao.response.SessionResponseDTO;
import com.daimainardi.desafiovotacao.response.VoteResultDTO;
import com.daimainardi.desafiovotacao.stub.StubBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {
    @InjectMocks
    private SessionService sessionService;
    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private VoteRepository voteRepository;
    @Mock
    private AgendaService agendaService;

    @Test
    @DisplayName("Deve cadastrar nova sessão, não deve dar exceção")
    void shouldRegisterSession() {
        BDDMockito.given(sessionRepository.save(any(SessionEntity.class)))
                .willReturn(SessionMapper.mapRequestToEntity(StubBuilder.sessionRequestDTO()));
        when(agendaService.existsAgendaById(StubBuilder.agendaEntity().id())).thenReturn(true);
        when(agendaService.findAgendaById(anyString())).thenReturn(StubBuilder.agendaEntity());
        SessionResponseDTO session = sessionService.createSession(StubBuilder.sessionRequestDTO());
        Assertions.assertEquals("Aumento de salário", session.agendaTitle());
        Assertions.assertEquals(30, session.durationMinutes());
        BDDMockito.verify(sessionRepository, Mockito.times(1)).save(any(SessionEntity.class));
    }

    @Test
    @DisplayName("Deve cadastrar uma sessão com duração de um minuto quando o tempo de duração não for passado pelo request")
    void shouldRegisterSessionLastingOneMinuteWhenItIsNotPassedByTheRequest() {
        SessionRequestDTO sessionRequestDTO = new SessionRequestDTO("123456", 0);
        SessionEntity sessionEntity = new SessionEntity("78910", "123456", LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(sessionRequestDTO.durationMinutes()));
        BDDMockito.given(sessionRepository.save(any(SessionEntity.class))).willReturn(sessionEntity);
        when(agendaService.existsAgendaById(StubBuilder.agendaEntity().id())).thenReturn(true);
        when(agendaService.findAgendaById(anyString())).thenReturn(StubBuilder.agendaEntity());
        SessionResponseDTO session = sessionService.createSession(sessionRequestDTO);
        Assertions.assertEquals("Aumento de salário", session.agendaTitle());
        Assertions.assertEquals(1, session.durationMinutes());
        BDDMockito.verify(sessionRepository, Mockito.times(1)).save(any(SessionEntity.class));
    }

    @Test
    @DisplayName("Deve cadastrar um voto, não deve dar exceção")
    void shouldRegisterVote() {
        BDDMockito.given(voteRepository.save(any(VoteEntity.class)))
                .willReturn(VoteMapper.mapRequestToEntity(StubBuilder.voteRequestDTO()));
        BDDMockito.given(sessionRepository.findById(anyString())).willReturn(Optional.of(StubBuilder.sessionEntity()));
        Assertions.assertDoesNotThrow(() -> sessionService.createVote(StubBuilder.voteRequestDTO()));
        BDDMockito.verify(voteRepository, BDDMockito.times(1)).save(any(VoteEntity.class));
    }

    @Test
    @DisplayName("Deve encontar uma sessão ativa pelo id")
    void shouldFindActiveSessionById() {
        BDDMockito.given(sessionRepository.findById(StubBuilder.sessionEntity().id())).willReturn(Optional.of(StubBuilder.sessionEntity()));
        SessionEntity session= Assertions.assertDoesNotThrow(() -> sessionService.findActiveSessionById(StubBuilder.sessionEntity().id()));
        Assertions.assertEquals("123456", session.agendaId());
        BDDMockito.verify(sessionRepository, BDDMockito.times(1)).findById(StubBuilder.sessionEntity().id());
    }

    @Test
    @DisplayName("Não deve encontrar sessão pelo id, Id inexistente, SessionNotFoundException")
    void shouldNotFindSessionByIdNotFound() {
        String sessionId = StubBuilder.sessionEntity().id();
        BDDMockito.given(sessionRepository.findById(sessionId))
                .willThrow(new SessionNotFoundException("Session not found", HttpStatus.NOT_FOUND));
        SessionNotFoundException exception = Assertions.assertThrows(SessionNotFoundException.class,
                () -> sessionService.findActiveSessionById(sessionId));
        Assertions.assertEquals(SessionNotFoundException.class, exception.getClass());
        Assertions.assertEquals("Session not found", exception.getMessage());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        BDDMockito.verify(sessionRepository, BDDMockito.times(1)).findById(sessionId);
    }

    @Test
    @DisplayName("Não deve encontrar sessão pelo id, sessão inativa, SessionNotActiveException")
    void shouldNotFindSessionActiveById() {
        String sessionId = StubBuilder.sessionEntity().id();
        BDDMockito.given(sessionRepository.findById(sessionId)).willThrow(new SessionNotActiveException("Session not active", HttpStatus.BAD_REQUEST));
        SessionNotActiveException exception = Assertions.assertThrows(SessionNotActiveException.class,
                () -> sessionService.findActiveSessionById(sessionId));
        Assertions.assertEquals(SessionNotActiveException.class, exception.getClass());
        Assertions.assertEquals("Session not active", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        BDDMockito.verify(sessionRepository, BDDMockito.times(1)).findById(sessionId);
    }

    @Test
    @DisplayName("Deve retornar uma lista somente das sessões ativas")
    void shouldFindAllActiveSessions() {
        List<SessionEntity> sessionEntityList = List.of(StubBuilder.sessionEntity());
        BDDMockito.given(sessionRepository.findAll()).willReturn(sessionEntityList);
        BDDMockito.given(agendaService.findAgendaById(anyString())).willReturn(StubBuilder.agendaEntity());
        List<SessionResponseDTO> sessionResponseDTOList = sessionService.findAllActiveSessions();
        BDDMockito.verify(sessionRepository, BDDMockito.times(1)).findAll();
        Assertions.assertNotNull(sessionResponseDTOList);
        Assertions.assertEquals("Aumento de salário", sessionResponseDTOList.getFirst().agendaTitle());
        Assertions.assertEquals(1, sessionResponseDTOList.size());
    }

    @Test
    @DisplayName("Deve retornar o resultado da votação")
    void shouldShowTheFinalVotingResult() {
        List<VoteEntity> voteEntityList = List.of(StubBuilder.voteEntityYes(), StubBuilder.voteEntityYes(),
                StubBuilder.voteEntityYes(), StubBuilder.voteEntityYes(), StubBuilder.voteEntityNo(),
                StubBuilder.voteEntityNo(), StubBuilder.voteEntityNo());
        var id = StubBuilder.sessionEntity().id();
        BDDMockito.given(voteRepository.findAllBySessionId(id)).willReturn(voteEntityList);
        VoteResultDTO result = sessionService.finalVotingResult(id);
        BDDMockito.verify(voteRepository, BDDMockito.times(1)).findAllBySessionId(id);
        Assertions.assertNotNull(result);
        Assertions.assertEquals("78910", result.sessionId());
        Assertions.assertEquals(4, result.countVotesYes());
        Assertions.assertEquals(3, result.countVotesNo());
    }
}

