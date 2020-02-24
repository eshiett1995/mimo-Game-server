package com.gambeat.mimo.server.model;

public class Enum {

    public enum MatchType{
        Single,
        RoyalRumble
    }

    public enum LoginType{
        Facebook,
        Google
    }

    public enum MatchStatus{
        Started,
        Ended,
        Pending,
    }

    public enum MatchState{
        Open,
        Close
    }
}
