package com.gambeat.mimo.server.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
public class Match extends Default {
    private String name;
    private boolean isCompetition;
    private long startTime;
    private Long entryFee;
    private Enum.MatchType matchType;
    private Enum.MatchStatus matchStatus;
    private Enum.MatchState matchState;
    private ArrayList<MatchSeat> matchSeat = new ArrayList<>();
    private ArrayList<MatchSeat> winners = new ArrayList<>();
    private List<StageObject> stageObjects = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCompetition() {
        return isCompetition;
    }

    public void setCompetition(boolean competition) {
        isCompetition = competition;
    }

    public Long getEntryFee() {
        return entryFee;
    }

    public void setEntryFee(Long entryFee) {
        this.entryFee = entryFee;
    }

    public ArrayList<MatchSeat> getWinners() {
        return winners;
    }

    public void setWinners(ArrayList<MatchSeat> winners) {
        this.winners = winners;
    }

    public Enum.MatchStatus getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(Enum.MatchStatus matchStatus) {
        this.matchStatus = matchStatus;
    }

    public Enum.MatchType getMatchType() {
        return matchType;
    }

    public void setMatchType(Enum.MatchType matchType) {
        this.matchType = matchType;
    }

    public ArrayList<MatchSeat> getMatchSeat() {
        return matchSeat;
    }

    public void setMatchSeat(ArrayList<MatchSeat> matchSeat) {
        this.matchSeat = matchSeat;
    }

    public List<StageObject> getStageObjects() {
        return stageObjects;
    }

    public void setStageObjects(List<StageObject> stageObjects) {
        this.stageObjects = stageObjects;
    }

    public Enum.MatchState getMatchState() {
        return matchState;
    }

    public void setMatchState(Enum.MatchState matchState) {
        this.matchState = matchState;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
