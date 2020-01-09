package com.gambeat.mimo.server.repository;

import com.gambeat.mimo.server.model.Competition;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CompetitionRepository extends MongoRepository<Competition, String> {

}

