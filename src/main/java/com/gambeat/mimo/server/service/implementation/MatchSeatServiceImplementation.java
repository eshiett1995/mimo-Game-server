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

        if(matchSeats.isEmpty()){
            return matchSeats;
        }

        Collections.sort(matchSeats);

        int highestScore = matchSeats.get(0).getPoints().isEmpty() ? 0 :Collections.max(matchSeats.get(0).getPoints());
        int lastScore = highestScore;
        int lastPosition = 0;

        for (MatchSeat matchSeat : matchSeats) {

            int highScore = matchSeat.getPoints().isEmpty() ? 0 : Collections.max(matchSeat.getPoints());

            if (lastPosition == 0) {
                matchSeat.setPosition(lastPosition + 1);
                lastPosition = 1;

            } else if (highScore == lastScore) {
                matchSeat.setPosition(lastPosition);

            } else {
                matchSeat.setPosition(lastPosition + 1);
                lastPosition = lastPosition + 1;
            }

            lastScore = highestScore;
        }

        return matchSeats;
    }

    @Override
    public ArrayList<MatchSeat> getFirstPosition(ArrayList<MatchSeat> matchSeats) {
        ArrayList<MatchSeat> winners = new ArrayList<>();
        for (MatchSeat matchSeat : matchSeats) {
            if (winners.isEmpty()){
                winners.add(matchSeat);
            }else{
                int seatMaxPoint = matchSeat.getPoints().isEmpty() ? 0 : Collections.max(matchSeat.getPoints());
                int winnerMaxPoint = winners.get(0).getPoints().isEmpty() ? 0 : Collections.max(winners.get(0).getPoints());
                if(seatMaxPoint > winnerMaxPoint){
                    winners.clear();
                    winners.add(matchSeat);
                }else if(seatMaxPoint == winnerMaxPoint){
                    winners.add(matchSeat);
                }
            }
        }
        return winners;
    }

    @Override
    public ArrayList<MatchSeat> getNthPosition(ArrayList<MatchSeat> matchSeats, int position) {
        ArrayList<MatchSeat> winners = new ArrayList<>();

        for (MatchSeat matchSeat : matchSeats) {

            if (winners.isEmpty()){
                winners.add(matchSeat);

            }else{
                if(Collections.max(matchSeat.getPoints()) > Collections.max(winners.get(0).getPoints())){
                    winners.clear();
                    winners.add(matchSeat);
                }else if(Collections.max(matchSeat.getPoints()).equals(Collections.max(winners.get(0).getPoints()))){
                    winners.add(matchSeat);
                }
            }
        }

        return winners;
    }
}
