package com.gambeat.mimo.server.service.implementation;

import com.gambeat.mimo.server.model.MatchSeat;
import com.gambeat.mimo.server.service.MatchSeatService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;

@Service
public class MatchSeatServiceImplementation implements MatchSeatService {
    @Override
    public ArrayList<MatchSeat> givePosition(ArrayList<MatchSeat> matchSeats) {

        Collections.sort(matchSeats);

        int highestScore = Collections.max(matchSeats.get(0).getPoints());
        int lastScore = highestScore;
        int lastPosition = 0;

        for(int index = 0; index < matchSeats.size(); index++){

            int highScore = Collections.max(matchSeats.get(index).getPoints());

            if(lastPosition == 0){
                matchSeats.get(index).setPosition(lastPosition + 1);
                lastPosition = 1;

            }else if(highScore == lastScore){
                matchSeats.get(index).setPosition(lastPosition);

            }else {
                matchSeats.get(index).setPosition(lastPosition + 1);
                lastPosition = lastPosition + 1;
            }

            lastScore = highestScore;
        }

        return matchSeats;
    }
}
