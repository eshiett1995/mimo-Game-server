package com.gambeat.mimo.server.model;

public class Enum {

    public enum MatchType{
        Single,
        RoyalRumble
    }

    public enum Vendor{
        Paystack,
        WalletsAfrica,
        Payant,
        Gambeat
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
        Cashout,
        RoyalRumbleWinnerCashPrice
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
