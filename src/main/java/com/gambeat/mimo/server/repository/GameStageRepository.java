package com.gambeat.mimo.server.repository;

import com.gambeat.mimo.server.model.GameStage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface GameStageRepository extends MongoRepository<GameStage,String> {

    Optional<GameStage> findByMatch_Id(String matchId);
}
