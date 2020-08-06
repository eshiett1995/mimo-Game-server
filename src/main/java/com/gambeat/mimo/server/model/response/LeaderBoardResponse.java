package com.gambeat.mimo.server.model.response;

import com.gambeat.mimo.server.model.Rank;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class LeaderBoardResponse extends ResponseModel{

    public LeaderBoardResponse(List<Rank> ranks, Rank rank){
        if(CollectionUtils.isEmpty(ranks)){
            this.ranks = Collections.EMPTY_LIST;
            this.hasRank = !Objects.isNull(rank);
            this.userRank = Objects.isNull(rank) ? new FormattedRank() : new FormattedRank(rank);
        }else{
            for(int index = 0; index < ranks.size(); index++){
                this.ranks.add(new FormattedRank(ranks.get(index)));
            }
            this.hasRank = !Objects.isNull(rank);
            this.userRank = Objects.isNull(rank) ? new FormattedRank() : new FormattedRank(rank);
            this.isOnList = ranks.contains(rank);
        }
    }

    public LeaderBoardResponse(boolean successful, String message){
        this.setSuccessful(successful);
        this.setMessage(message);
    }

    private List<FormattedRank> ranks = new ArrayList<>();

    private boolean hasRank;

    private FormattedRank userRank;

    private boolean isOnList;

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

    public boolean isHasRank() {
        return hasRank;
    }

    public void setHasRank(boolean hasRank) {
        this.hasRank = hasRank;
    }

    class FormattedRank {

        private String firstName;
        private String lastName;
        private String email;
        private String photoUrl;
        private long score;
        private long position;

        private FormattedRank(){
            this.firstName = "";
            this.lastName = "";
            this.email = "";
            this.score = 0;
            this.position = 0;
            this.photoUrl = "";
        }

        private FormattedRank(Rank rank){
            this.firstName = rank.getUser().getFirstName();
            this.lastName = rank.getUser().getLastName();
            this.email = rank.getUser().getEmail();
            this.score = rank.getScore();
            this.position = rank.getPosition();
            this.photoUrl = rank.getUser().getPhotoUrl();
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

        public String getPhotoUrl() {
            return photoUrl;
        }

        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }
    }
}
