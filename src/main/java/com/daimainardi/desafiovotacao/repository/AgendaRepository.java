package com.daimainardi.desafiovotacao.repository;

import com.daimainardi.desafiovotacao.entity.AgendaEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendaRepository extends MongoRepository<AgendaEntity, String> {
}
