package com.daimainardi.desafiovotacao.mapper;

import com.daimainardi.desafiovotacao.entity.VoteEntity;
import com.daimainardi.desafiovotacao.request.VoteRequestDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VoteMapper {

    public static VoteEntity mapRequestToEntity(VoteRequestDTO voteRequestDTO) {
        return VoteEntity.builder()
                .associateCpf(voteRequestDTO.associateCpf())
                .sessionId(voteRequestDTO.sessionId())
                .value(voteRequestDTO.value().toUpperCase())
                .build();
    }
}
