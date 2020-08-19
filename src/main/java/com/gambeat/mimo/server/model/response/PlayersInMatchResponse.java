package com.gambeat.mimo.server.model.response;

import com.gambeat.mimo.server.model.Match;
import com.gambeat.mimo.server.model.MatchSeat;

import java.util.ArrayList;
import java.util.Optional;

public class PlayersInMatchResponse extends ResponseModel{

    public PlayersInMatchResponse(Boolean isSuccessful, String msg){
        super(isSuccessful, msg);
    }

    public PlayersInMatchResponse(Match match){

        for (MatchSeat matchseat: match.getMatchSeat()) {
             Optional<MatchSeat> matchSeatOptional = match.getWinners().stream()
                     .filter(winner -> winner.getUser().getId().equals(matchseat.getUser().getId()))
                    .findFirst();

             if (matchSeatOptional.isPresent()){
                 this.getWinners().add(new Player(matchseat));
             }else {
                 this.getPlayers().add(new Player(matchseat));
             }
        }
    }

    private ArrayList<Player> winners = new ArrayList<>();

    private ArrayList<Player> players = new ArrayList<>();

    public ArrayList<Player> getWinners() {
        return winners;
    }

    public void setWinners(ArrayList<Player> winners) {
        this.winners = winners;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public class Player {

        private String firstName;
        private String lastName;
        private String email;
        private String photoUrl;
        private ArrayList<Integer> score = new ArrayList<Integer>();

        public Player(MatchSeat matchSeat){
            this.firstName = matchSeat.getUser().getFirstName();
            this.lastName = matchSeat.getUser().getLastName();
            this.email = matchSeat.getUser().getEmail();
            this.photoUrl = matchSeat.getUser().getPhotoUrl();
            this.score = matchSeat.getPoints();
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhotoUrl() {
            return photoUrl;
        }

        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }

        public ArrayList<Integer> getScore() {
            return score;
        }

        public void setScore(ArrayList<Integer> score) {
            this.score = score;
        }
    }
}
