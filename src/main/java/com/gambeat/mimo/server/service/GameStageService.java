package com.gambeat.mimo.server.service;

import com.gambeat.mimo.server.model.GameStage;
import com.gambeat.mimo.server.model.StageObject;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface GameStageService {

    List<StageObject> generateStage(int iteration);

    GameStage save(GameStage gameStage);

    Optional<GameStage> getGameStageByMatchId(String matchId);
}
