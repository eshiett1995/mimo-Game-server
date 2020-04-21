package com.gambeat.mimo.server.service;

import com.gambeat.mimo.server.model.StageObject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StageGeneratorService{

    List<StageObject> generateStage(int iteration);
}
