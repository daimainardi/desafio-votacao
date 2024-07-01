package com.daimainardi.desafiovotacao.response;

public record VoteResultDTO(
        String sessionId,
        long countVotesYes,
        long countVotesNo) {
}
