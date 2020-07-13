package com.gambeat.mimo.server.model;


import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
public class GameStage {
    private List<StageObject> stageObjects = new ArrayList<>();
    @DBRef
    private Match match;

    public List<StageObject> getStageObjects() {
        return stageObjects;
    }

    public void setStageObjects(List<StageObject> stageObjects) {
        this.stageObjects = stageObjects;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }
}
