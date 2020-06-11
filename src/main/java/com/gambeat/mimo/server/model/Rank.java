package com.gambeat.mimo.server.model;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Rank extends Default {

    @DBRef
    private User user;
    private long score;
    private long weeklyScore;
    private  long position;

    public long getWeeklyScore() {
        return weeklyScore;
    }

    public void setWeeklyScore(long weeklyScore) {
        this.weeklyScore = weeklyScore;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
