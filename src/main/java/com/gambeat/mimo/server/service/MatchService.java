package com.gambeat.mimo.server.service;

import com.gambeat.mimo.server.model.Match;
import com.gambeat.mimo.server.model.MatchSeat;
import com.gambeat.mimo.server.model.User;
import com.gambeat.mimo.server.model.request.MatchCreationRequest;
import com.gambeat.mimo.server.model.request.RoyalRumbleSearchRequest;
import com.gambeat.mimo.server.model.response.RoyalRumbleSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public interface MatchService {
    Optional<Match> findById(String id);
    String generateMatchName(User user);
    Match save(Match match);
    Match update(Match match);
    void delete(Match match);
    Boolean isBelowMinimumAmount(long entryFee);
    Boolean hasUserAlreadyJoined(User user, Match match);
    Boolean hasExceedDuration(Match match);
    Boolean hasPendingMatch(User user);
    Optional<Match> getPlayableRoyalRumbleMatch(long entryFee);
    Match createRoyalRumbleMatch(User user, MatchCreationRequest matchCreationRequest);
    Page<Match> getActiveRoyalRumbleMatches(Pageable pageable);
    Page<Match> getActiveRoyalRumbleMatches(Pageable pageable, RoyalRumbleSearchRequest royalRumbleSearchRequest);
    ArrayList<RoyalRumbleSearchResponse.FormattedMatch> tagRegisteredMatch(ArrayList<String> pendingMatch, ArrayList<RoyalRumbleSearchResponse.FormattedMatch> matches);
    ArrayList<RoyalRumbleSearchResponse.FormattedMatch> tagRegisteredMatchFromStringArray(ArrayList<String> pendingMatchIds, ArrayList<RoyalRumbleSearchResponse.FormattedMatch> matches);
    void endRoyalRumbleMatchesCronJob();
    void giveMoneyToFirstPositionWinners(Match match);
    void updatePlayersStats(Match match);
    void updateMatchSeatToHasStarted(Match match, MatchSeat matchSeat);
    void updateMatchSeatToHasFinished(Match match, MatchSeat matchSeat);
}
