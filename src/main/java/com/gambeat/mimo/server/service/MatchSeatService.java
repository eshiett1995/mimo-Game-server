package com.gambeat.mimo.server.service;

import com.gambeat.mimo.server.model.MatchSeat;

import java.util.ArrayList;

public interface MatchSeatService {

    ArrayList<MatchSeat> givePosition(ArrayList<MatchSeat> matchSeats);

    ArrayList<MatchSeat> getFirstPosition(ArrayList<MatchSeat> matchSeats);

    ArrayList<MatchSeat> getNthPosition(ArrayList<MatchSeat> matchSeats, int position);
}
