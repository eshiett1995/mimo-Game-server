package com.gambeat.mimo.server.model;

public class Enum {

    public enum MatchType{
        Single,
        RoyalRumble
    }

    public enum TransactionType{
        Debit,
        Credit,
    }

    public enum PaymentOption{
        EntryFee,
        GambeatFee,
        Reversal,
        Topup,
        RoyalRumbleAward
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
