package com.gambeat.mimo.server.model;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Collections;

@Document
public class MatchSeat implements Comparable {

    public MatchSeat(){}

    public MatchSeat(User user){
        this.user = user;
    }

    @DBRef
    private User user;

    private ArrayList<Integer> points = new ArrayList<>();

    private int position;

    private boolean hasStarted;

    private boolean hasFinished;



    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<Integer> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Integer> points) {
        this.points = points;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isHasStarted() {
        return hasStarted;
    }

    public void setHasStarted(boolean hasStarted) {
        this.hasStarted = hasStarted;
    }

    public boolean isHasFinished() {
        return hasFinished;
    }

    public void setHasFinished(boolean hasFinished) {
        this.hasFinished = hasFinished;
    }

    @Override
    public int compareTo(Object matchSeat) {
        Collections.sort(((MatchSeat)matchSeat).getPoints(), Collections.reverseOrder());
        int score = ((MatchSeat) matchSeat).points.get(0);

        Collections.sort(this.getPoints(), Collections.reverseOrder());
        int presentScore = 0;
        if(!this.getPoints().isEmpty()){
            presentScore =  this.getPoints().get(0);
        }
        return score - presentScore;
    }
}
