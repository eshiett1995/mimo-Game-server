package com.gambeat.mimo.server.repository;

import com.gambeat.mimo.server.model.Match;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MatchRepository extends MongoRepository<Match, String> {
}
