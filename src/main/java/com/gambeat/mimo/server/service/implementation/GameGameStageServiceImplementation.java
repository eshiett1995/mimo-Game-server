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
import java.util.Random;

@Service
public class GameGameStageServiceImplementation implements GameStageService {

    @Autowired
    GameStageRepository gameStageRepository;

    @Override
    public List<StageObject> generateStage(int iteration) {
        List<StageObject> stageObjects = new ArrayList<>();
        int spikeInterval = 0;
        for (int index = 1; index <= iteration; index++) {
            int rand = new Random().nextInt(6);
            String instance;
            spikeInterval++;

            if (rand == 1 && spikeInterval > 1) {
                instance = "spikes";
                spikeInterval = 0;

            } else if (rand == 2) {
                instance = "breakPlatform";
            } else {
                instance = "platform";
            }

            //float random = min + r.nextFloat() * (max - min);
            // int random = minimum + rn.nextInt(maxValue - minvalue + 1)
            float minimumNumber = (float) (720 / 8.5);
            float maximumValue = (float) (720 / 1.18);


            //float randPos = new Random().nextFloat((float)(720 / 8.5), (float)(720 / 1.18));
            float xCoordinate = minimumNumber + new Random().nextFloat() * (maximumValue - minimumNumber);
            StageObject stageObject = new StageObject();
            stageObject.setCoordinate(xCoordinate);
            stageObject.setObject(instance);
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
