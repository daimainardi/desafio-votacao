package com.daimainardi.desafiovotacao.repository;

import com.daimainardi.desafiovotacao.entity.SessionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends MongoRepository<SessionEntity, String> {
}
