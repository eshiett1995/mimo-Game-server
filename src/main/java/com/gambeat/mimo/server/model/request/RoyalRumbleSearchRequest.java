package com.gambeat.mimo.server.model.request;

public class RoyalRumbleSearchRequest {
    private String competitionName;
    private long minimumAmount;
    private long maximumAmount;
    private int minimumPlayers;
    private int maximumPlayers;
    private String sortField;
    private boolean ascending;
    private boolean filter = false;

    public String getCompetitionName() {
        return competitionName;
    }

    public void setCompetitionName(String competitionName) {
        this.competitionName = competitionName;
    }

    public double getMinimumAmount() {
        return minimumAmount;
    }

    public void setMinimumAmount(long minimumAmount) {
        this.minimumAmount = minimumAmount;
    }

    public double getMaximumAmount() {
        return maximumAmount;
    }

    public void setMaximumAmount(long maximumAmount) {
        this.maximumAmount = maximumAmount;
    }

    public double getMinimumPlayers() {
        return minimumPlayers;
    }

    public void setMinimumPlayers(int minimumPlayers) {
        this.minimumPlayers = minimumPlayers;
    }

    public double getMaximumPlayers() {
        return maximumPlayers;
    }

    public void setMaximumPlayers(int maximumPlayers) {
        this.maximumPlayers = maximumPlayers;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    public boolean isFilter() {
        return filter;
    }

    public void setFilter(boolean filter) {
        this.filter = filter;
    }
}
