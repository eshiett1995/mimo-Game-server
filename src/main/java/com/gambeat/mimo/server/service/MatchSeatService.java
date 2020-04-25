package com.gambeat.mimo.server.service;

import com.gambeat.mimo.server.model.MatchSeat;

import java.util.ArrayList;

public interface MatchSeatService {

    ArrayList<MatchSeat> givePosition(ArrayList<MatchSeat> matchSeats);
}
