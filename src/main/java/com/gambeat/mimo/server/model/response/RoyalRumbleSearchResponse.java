package com.gambeat.mimo.server.model.response;

import com.gambeat.mimo.server.model.Enum;
import com.gambeat.mimo.server.model.Match;
import com.gambeat.mimo.server.model.MatchSeat;
import com.google.gson.Gson;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;

public class RoyalRumbleSearchResponse  extends ResponseModel{
    private int number;
    private int size;
    private int numberOfElements;
    private ArrayList<FormattedMatch> content = new ArrayList<>();
    private boolean hasContent;
    private boolean isFirst;
    private boolean isLast;
    private boolean hasNext;
    private boolean hasPrevious;
    private int totalPages;
    private long getTotalElements;

    public RoyalRumbleSearchResponse(Page<Match> matches){
        super(true, "Successful");
        this.number = matches.getNumber();
        this.size =  matches.getSize();
        this.numberOfElements = matches.getNumberOfElements();
        matches.getContent().forEach(match -> this.content.add(new FormattedMatch(match)));

//        for(int index = 0; index < matches.getContent().size(); index++){
//            //this.content.add(new FormattedMatch(matches.getContent().get(index)));
//            //new Gson().toJson(matches.getContent().get(index));
//            new FormattedMatch(matches.getContent().get(index));
//        }
        this.hasContent = matches.hasContent();
        this.isFirst = matches.isFirst();
        this.isLast = matches.isLast();
        this.hasNext = matches.hasNext();
        this.hasPrevious = matches.hasPrevious();
        this.totalPages = matches.getTotalPages();
        this.getTotalElements = matches.getTotalElements();
    }

    public RoyalRumbleSearchResponse(boolean successful, String message) {
        super(successful, message);
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public ArrayList<FormattedMatch> getContent() {
        return content;
    }

    public void setContent(ArrayList<FormattedMatch> content) {
        this.content = content;
    }

    public boolean isHasContent() {
        return hasContent;
    }

    public void setHasContent(boolean hasContent) {
        this.hasContent = hasContent;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public boolean isHasPrevious() {
        return hasPrevious;
    }

    public void setHasPrevious(boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getGetTotalElements() {
        return getTotalElements;
    }

    public void setGetTotalElements(long getTotalElements) {
        this.getTotalElements = getTotalElements;
    }

    class FormattedMatch {

        private String id;
        private String name;
        private Long entryFee;
        private Enum.MatchStatus matchStatus;
        private Enum.MatchState matchState;
        private int numberOfCompetitors;
        private String winners;

        private FormattedMatch(Match match) {

            this.id = match.getId();
            this.name = match.getName();
            this.entryFee = match.getEntryFee();
            this.matchStatus = match.getMatchStatus();
            this.matchState = match.getMatchState();
            this.numberOfCompetitors = match.getMatchSeat().size();
            this.winners =  parseWinners(match.getWinners());

        }

        private String parseWinners(List<MatchSeat> matchSeats){
            String winners = "";
            matchSeats.forEach(matchSeat -> {
                winners.concat(matchSeat.getUser().getEmail() + "");
            });
            return winners;
        }
    }
}
