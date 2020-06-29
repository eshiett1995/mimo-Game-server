package com.gambeat.mimo.server.service.implementation;

import com.gambeat.mimo.server.model.GambeatSystem;
import com.gambeat.mimo.server.model.Wallet;
import com.gambeat.mimo.server.repository.GambeatSystemRepository;
import com.gambeat.mimo.server.service.GambeatSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GambeatSystemServiceImplementation implements GambeatSystemService {

    @Autowired
    GambeatSystemRepository gambeatSystemRepository;


    @Override
    public Wallet getGambeatWallet() {
        List<GambeatSystem> gambeatSystems = gambeatSystemRepository.findAll();
        return gambeatSystems.get(0).getUser().getWallet();
    }
}
