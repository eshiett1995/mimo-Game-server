package com.gambeat.mimo.server.service;

import com.gambeat.mimo.server.model.GambeatSystem;
import com.gambeat.mimo.server.model.Wallet;

import java.util.List;

public interface GambeatSystemService {

    Wallet getGambeatWallet();

    List<GambeatSystem> findAll();

    GambeatSystem save(GambeatSystem gambeatSystem);
}
