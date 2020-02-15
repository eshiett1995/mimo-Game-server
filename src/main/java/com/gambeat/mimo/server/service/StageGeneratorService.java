package com.gambeat.mimo.server.service;

import com.gambeat.mimo.server.model.StageGeneratorObject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StageGeneratorService{

    List<StageGeneratorObject> generateStage(int iteration);
}
