package com.daimainardi.desafiovotacao.mapper;

import com.daimainardi.desafiovotacao.entity.AgendaEntity;
import com.daimainardi.desafiovotacao.request.AgendaRequestDTO;
import com.daimainardi.desafiovotacao.response.AgendaResponseDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AgendaMapper {

    public static AgendaEntity mapRequestToEntity(AgendaRequestDTO agendaRequestDTO) {
        return AgendaEntity.builder()
                .title(agendaRequestDTO.title())
                .description(agendaRequestDTO.description())
                .creationDate(LocalDateTime.now())
                .build();
    }

    public static AgendaResponseDTO mapEntityToResponse(AgendaEntity agendaEntity){
        return new AgendaResponseDTO(agendaEntity.id(), agendaEntity.title(), agendaEntity.description());
    }

    public static List<AgendaResponseDTO> mapEntityListToResponseList(List<AgendaEntity> agendaEntityList){
        return agendaEntityList.stream()
                .map(agendaEntity -> new AgendaResponseDTO(agendaEntity.id(), agendaEntity.title(), agendaEntity.description()))
                .toList();
    }

}
