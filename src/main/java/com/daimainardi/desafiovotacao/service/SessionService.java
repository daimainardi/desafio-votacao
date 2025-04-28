package com.daimainardi.desafiovotacao.service;

import com.daimainardi.desafiovotacao.entity.SessionEntity;
import com.daimainardi.desafiovotacao.entity.VoteEntity;
import com.daimainardi.desafiovotacao.exception.AgendaNotFoundException;
import com.daimainardi.desafiovotacao.exception.SessionNotActiveException;
import com.daimainardi.desafiovotacao.exception.SessionNotFoundException;
import com.daimainardi.desafiovotacao.mapper.SessionMapper;
import com.daimainardi.desafiovotacao.mapper.VoteMapper;
import com.daimainardi.desafiovotacao.repository.SessionRepository;
import com.daimainardi.desafiovotacao.repository.VoteRepository;
import com.daimainardi.desafiovotacao.request.SessionRequestDTO;
import com.daimainardi.desafiovotacao.request.VoteRequestDTO;
import com.daimainardi.desafiovotacao.response.SessionResponseDTO;
import com.daimainardi.desafiovotacao.response.VoteResultDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionService {

    private final SessionRepository sessionRepository;
    private final VoteRepository voteRepository;
    private final AgendaService agendaService;

    public SessionResponseDTO createSession(SessionRequestDTO sessionRequestDTO) {
        existsAgendaById(sessionRequestDTO);
        SessionRequestDTO session = validateSessionDurationTime(sessionRequestDTO);
        SessionEntity sessionEntity = sessionRepository.save(SessionMapper.mapRequestToEntity(session));
        return new SessionResponseDTO(sessionEntity.id(), getTitleByAgendaId(sessionEntity.agendaId()), session.durationMinutes());
    }

    public SessionEntity findActiveSessionById(String id) {
        var sessionEntity = sessionRepository.findById(id).orElseThrow(() ->
                new SessionNotFoundException("Session not found", HttpStatus.NOT_FOUND));

        if (!sessionEntity.endDateTime().isAfter(LocalDateTime.now())) {
            throw new SessionNotActiveException("Session not active", HttpStatus.BAD_REQUEST);
        }
        return sessionEntity;
    }

    public List<SessionResponseDTO> findAllActiveSessions() {
        return mapEntityListToResponseList(sessionRepository.findAll());
    }

    public void createVote(VoteRequestDTO voteRequestDTO) {
        findActiveSessionById(voteRequestDTO.sessionId());
        voteRepository.save(VoteMapper.mapRequestToEntity(voteRequestDTO));
    }

    public VoteResultDTO finalVotingResult(String sessionId){
        List<VoteEntity> voteEntityList = voteRepository.findAllBySessionId(sessionId);
        var sim = voteEntityList.stream().filter(voteEntity -> voteEntity.value().equals("SIM")).count();
        var nao = voteEntityList.stream().filter(voteEntity -> voteEntity.value().equals("NAO")).count();
        return new VoteResultDTO(sessionId, sim, nao);
    }

    private String getTitleByAgendaId(String agendaId) {
        return agendaService.findAgendaById(agendaId).title();
    }

    private List<SessionResponseDTO> mapEntityListToResponseList(List<SessionEntity> sessionEntityList) {
        return sessionEntityList.stream()
                .filter(sessionEntity -> sessionEntity.endDateTime().isAfter(LocalDateTime.now()))
                .map(this::mapToResponse)
                .toList();
    }

    private SessionResponseDTO mapToResponse(SessionEntity sessionEntity) {
        LocalDateTime endDateTime = sessionEntity.endDateTime();
        LocalDateTime startDateTime = sessionEntity.startDateTime();
        long durationMinutes = ChronoUnit.MINUTES.between(startDateTime, endDateTime);
        return new SessionResponseDTO(sessionEntity.id(), getTitleByAgendaId(sessionEntity.agendaId()), (int) durationMinutes);
    }

    private SessionRequestDTO validateSessionDurationTime(SessionRequestDTO session){
        if (Objects.isNull(session.durationMinutes()) || session.durationMinutes() == 0) {
            session = session.toBuilder().durationMinutes(1).build();
        }
        return session;
    }
    private void existsAgendaById(SessionRequestDTO session) {
        boolean agendaExists = agendaService.existsAgendaById(session.agendaId());
        if(!agendaExists){
            throw new AgendaNotFoundException("Agenda not found", HttpStatus.NOT_FOUND);
        }
    }
}


