package com.gambeat.mimo.server.service.implementation;


import com.gambeat.mimo.server.model.Enum;
import com.gambeat.mimo.server.model.Match;
import com.gambeat.mimo.server.model.MatchSeat;
import com.gambeat.mimo.server.model.User;
import com.gambeat.mimo.server.model.request.RoyalRumbleSearchRequest;
import com.gambeat.mimo.server.repository.MatchRepository;
import com.gambeat.mimo.server.service.MatchService;
import com.gambeat.mimo.server.service.StageGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MatchServiceImplementation implements MatchService {

    @Value("${minimum.amount}")
    private long minimumAmount;

    @Autowired
    MatchRepository matchRepository;

    @Autowired
    StageGeneratorService stageGeneratorService;

    @Override
    public Optional<Match> findById(String id) {
        return matchRepository.findById(id);
    }

    @Override
    public Match save(Match match) {
        return matchRepository.save(match);
    }

    @Override
    public Match update(Match match) {
        return matchRepository.save(match);
    }

    @Override
    public Boolean isBelowMinimumAmount(long entryFee) {
        return minimumAmount > entryFee;
    }

    @Override
    public Boolean hasPendingMatch(User user) {
        Boolean hasPendingMatch = false;
        if(matchRepository.findByMatchSeatUserAndMatchStatus(user, Enum.MatchStatus.Pending).isPresent()){
            hasPendingMatch = true;
        }

        if(matchRepository.findByMatchSeatUserAndMatchStatus(user, Enum.MatchStatus.Started).isPresent()){
            hasPendingMatch = true;
        }
        return hasPendingMatch;
    }

    @Override
    public Optional<Match> getPlayableRoyalRumbleMatch(long entryFee) {
        Optional<Match> optionalMatch = matchRepository.findMatchByMatchTypeAndEntryFeeGreaterThanAndAndMatchStatus(Enum.MatchType.RoyalRumble, entryFee, Enum.MatchStatus.Started);
        if(optionalMatch.isPresent()){
            return optionalMatch;
        }else{
            return matchRepository.findMatchByMatchTypeAndEntryFeeGreaterThanAndAndMatchStatus(Enum.MatchType.RoyalRumble, entryFee, Enum.MatchStatus.Pending);
        }
    }

    @Override
    public Match createRoyalRumbleMatch(User user, long entryFee) {
        MatchSeat matchSeat = new MatchSeat();
        matchSeat.setUser(user);

        Match match = new Match();
        match.setCompetition(false);
        match.setEntryFee(entryFee);
        match.getMatchSeat().add(matchSeat);
        match.setMatchType(Enum.MatchType.RoyalRumble);
        match.setName("random"); //todo add name creator function
        match.setStageGeneratorObjects(stageGeneratorService.generateStage(1000));
        match.setMatchStatus(Enum.MatchStatus.Started);
        return this.save(match);
    }

    @Override
    public Page<Match> getActiveRoyalRumbleMatches(Pageable pageable) {
        return matchRepository.getAllByMatchTypeAndMatchState(Enum.MatchType.RoyalRumble, Enum.MatchState.Open, pageable);
    }

    @Override
    public Page<Match> getActiveRoyalRumbleMatches(Pageable pageable, RoyalRumbleSearchRequest royalRumbleSearchRequest) {
        return matchRepository.getAllByMatchTypeAndMatchState(Enum.MatchType.RoyalRumble, Enum.MatchState.Open, pageable);
    }
}
