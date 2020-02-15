package com.gambeat.mimo.server.service.implementation;

import com.gambeat.mimo.server.model.StageGeneratorObject;
import com.gambeat.mimo.server.service.StageGeneratorService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StageGenerationImplementation implements StageGeneratorService {
    @Override
    public List<StageGeneratorObject> generateStage(int iteration) {
        List<StageGeneratorObject> stageGeneratorObjects = new ArrayList<>();
        double max = 720 / 1.18;
        double min = 720 / 8.5;
        for(int index = 0; index < iteration; index++){
            double xCoordinate = (Math.random()*((max-min)+1))+min;
            StageGeneratorObject stageGeneratorObject = new StageGeneratorObject();
            stageGeneratorObject.setCoordinate(xCoordinate);
            stageGeneratorObject.setObject(StageGeneratorObject.GameObject.BreakableLedge);
            stageGeneratorObject.setHasLife(false);
            stageGeneratorObjects.add(stageGeneratorObject);
        }
        return stageGeneratorObjects;
    }
}
