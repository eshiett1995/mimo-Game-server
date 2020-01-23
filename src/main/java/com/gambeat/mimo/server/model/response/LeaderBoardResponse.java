package com.gambeat.mimo.server.model.response;

import com.gambeat.mimo.server.model.Rank;

import java.util.List;

public class LeaderBoardResponse {

    private List<FormattedRank> ranks;

    private FormattedRank userRank;

    private boolean isOnList;

    public LeaderBoardResponse(List<Rank> ranks, Rank rank){

        for(int index = 0; index < ranks.size(); index++){
            this.ranks.add(new FormattedRank(ranks.get(index)));
        }
        this.userRank = new FormattedRank(rank);
        this.isOnList = ranks.contains(rank);
    }

    public List<FormattedRank> getRanks() {
        return ranks;
    }

    public void setRanks(List<FormattedRank> ranks) {
        this.ranks = ranks;
    }

    public FormattedRank getUserRank() {
        return userRank;
    }

    public void setUserRank(FormattedRank userRank) {
        this.userRank = userRank;
    }

    public boolean isOnList() {
        return isOnList;
    }

    public void setOnList(boolean onList) {
        isOnList = onList;
    }

    class FormattedRank {

        private String firstname;
        private String lastname;
        private String email;
        private long score;
        private long position;

        public FormattedRank(Rank rank){
            this.firstname = rank.getUser().getFirstName();
            this.lastname = rank.getUser().getLastName();
            this.email = rank.getUser().getEmail();
            this.score = rank.getScore();
            this.position = rank.getPosition();
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public long getScore() {
            return score;
        }

        public void setScore(long score) {
            this.score = score;
        }

        public long getPosition() {
            return position;
        }

        public void setPosition(long position) {
            this.position = position;
        }
    }
}
