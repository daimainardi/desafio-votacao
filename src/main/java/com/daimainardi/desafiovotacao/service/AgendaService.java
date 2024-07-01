package com.daimainardi.desafiovotacao.service;

import com.daimainardi.desafiovotacao.entity.AgendaEntity;
import com.daimainardi.desafiovotacao.exception.AgendaNotFoundException;
import com.daimainardi.desafiovotacao.mapper.AgendaMapper;
import com.daimainardi.desafiovotacao.repository.AgendaRepository;
import com.daimainardi.desafiovotacao.request.AgendaRequestDTO;
import com.daimainardi.desafiovotacao.response.AgendaResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AgendaService {

    private final AgendaRepository agendaRepository;

    public AgendaResponseDTO createAgenda(AgendaRequestDTO agendaRequestDTO) {
        AgendaEntity agendaEntity = agendaRepository.save(AgendaMapper.mapRequestToEntity(agendaRequestDTO));
        return AgendaMapper.mapEntityToResponse(agendaEntity);
    }

    public AgendaEntity findAgendaById(String id) {
        return agendaRepository.findById(id).orElseThrow(() ->
                new AgendaNotFoundException("Agenda not found", HttpStatus.NOT_FOUND));
    }

    public List<AgendaResponseDTO> findAll() {
        return AgendaMapper.mapEntityListToResponseList(agendaRepository.findAll());
    }
}
