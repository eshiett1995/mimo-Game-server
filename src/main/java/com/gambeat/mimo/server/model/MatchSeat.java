package com.gambeat.mimo.server.model;

import java.util.ArrayList;

public class MatchSeat {
    private User user;
    private ArrayList<Integer> points;

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
}
