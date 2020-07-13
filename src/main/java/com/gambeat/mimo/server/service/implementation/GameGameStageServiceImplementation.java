package com.gambeat.mimo.server.service.implementation;

import com.gambeat.mimo.server.model.GameStage;
import com.gambeat.mimo.server.model.StageObject;
import com.gambeat.mimo.server.repository.GameStageRepository;
import com.gambeat.mimo.server.service.GameStageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GameGameStageServiceImplementation implements GameStageService {

    @Autowired
    GameStageRepository gameStageRepository;

    @Override
    public List<StageObject> generateStage(int iteration) {
        List<StageObject> stageObjects = new ArrayList<>();
        double max = 720 / 1.18;
        double min = 720 / 8.5;
        for(int index = 0; index < iteration; index++){
            double xCoordinate = (Math.random()*((max-min)+1))+min;
            StageObject stageObject = new StageObject();
            stageObject.setCoordinate(xCoordinate);
            stageObject.setObject(StageObject.GameObject.BreakableLedge);
            stageObject.setHasLife(false);
            stageObjects.add(stageObject);
        }
        return stageObjects;
    }

    @Override
    public GameStage save(GameStage gameStage) {
        return gameStageRepository.save(gameStage);
    }

    @Override
    public Optional<GameStage> getGameStageByMatchId(String matchId) {
        return gameStageRepository.findByMatch_Id(matchId);
    }
}
