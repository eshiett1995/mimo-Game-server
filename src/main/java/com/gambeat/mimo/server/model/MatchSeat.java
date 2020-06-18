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

    @Override
    public int compareTo(Object matchSeat) {
        Collections.sort(((MatchSeat)matchSeat).getPoints(), Collections.reverseOrder());
        int score = ((MatchSeat) matchSeat).points.get(0);

        Collections.sort(this.getPoints(), Collections.reverseOrder());
        int presentScore =  this.getPoints().get(0);

        return score - presentScore;
    }
}
