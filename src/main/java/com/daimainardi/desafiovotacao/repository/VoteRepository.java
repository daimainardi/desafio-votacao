package com.daimainardi.desafiovotacao.repository;

import com.daimainardi.desafiovotacao.entity.VoteEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends MongoRepository<VoteEntity, String> {

    List<VoteEntity> findAllBySessionId(String sessionId);
}
