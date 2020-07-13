package com.gambeat.mimo.server.service.implementation;


import com.gambeat.mimo.server.model.*;
import com.gambeat.mimo.server.model.Enum;
import com.gambeat.mimo.server.model.request.MatchCreationRequest;
import com.gambeat.mimo.server.model.request.RoyalRumbleSearchRequest;
import com.gambeat.mimo.server.model.response.RoyalRumbleSearchResponse;
import com.gambeat.mimo.server.repository.MatchRepository;
import com.gambeat.mimo.server.service.MatchSeatService;
import com.gambeat.mimo.server.service.MatchService;
import com.gambeat.mimo.server.service.GameStageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MatchServiceImplementation implements MatchService {

    @Value("${minimum.amount}")
    private long minimumAmount;

    @Value("${royal.rumble.time.limit.seconds}")
    private long royalRumbleTimeLimitSeconds;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    MatchRepository matchRepository;

    @Autowired
    MatchSeatService matchSeatService;

    @Autowired
    GameStageService gameStageService;

    @Override
    public Optional<Match> findById(String id) {
        return matchRepository.findById(id);
    }

    @Override
    public String generateMatchName(User user) {
        return user.getId();
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
    public void delete(Match match) {
        matchRepository.delete(match);
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
    public Match createRoyalRumbleMatch(User user, MatchCreationRequest matchCreationRequest) {
        MatchSeat matchSeat = new MatchSeat();
        matchSeat.setUser(user);

        Match match = new Match();
        match.setCompetition(false);
        match.setEntryFee(matchCreationRequest.getEntryFee());
        match.getMatchSeat().add(matchSeat);
        match.setCompetitorLimit(matchCreationRequest.getMaxPlayers());
        match.setMatchState(Enum.MatchState.Open);
        match.setMatchType(Enum.MatchType.RoyalRumble);
        match.setName(Objects.equals(matchCreationRequest.getMatchName(), "") ? this.generateMatchName(user) : matchCreationRequest.getMatchName());


        GameStage gameStage = new GameStage();
        gameStage.setStageObjects(gameStageService.generateStage(1000));

        match.setMatchStatus(Enum.MatchStatus.Started);
        Match savedMatch = this.save(match);
        gameStage.setMatch(savedMatch);
        gameStageService.save(gameStage);
        return savedMatch;
    }

    @Override
    public Page<Match> getActiveRoyalRumbleMatches(Pageable pageable) {
        return matchRepository.getAllByMatchTypeAndMatchState(Enum.MatchType.RoyalRumble, Enum.MatchState.Open, pageable);
    }

    @Override
    public Page<Match> getActiveRoyalRumbleMatches(Pageable pageable, RoyalRumbleSearchRequest royalRumbleSearchRequest) {

        if(!royalRumbleSearchRequest.isFilter()) {
            return matchRepository.getAllByMatchTypeAndMatchState(Enum.MatchType.RoyalRumble, Enum.MatchState.Open, pageable);
        }else{

            String sortField = royalRumbleSearchRequest.getSortField().isEmpty() ? "createdAt" : royalRumbleSearchRequest.getSortField();

            //todo make it look for only open ones
            Query query = new Query();
            query.addCriteria(
                    Criteria.where("name").regex(royalRumbleSearchRequest.getCompetitionName())
                    .and("entryFee")
                    .gte(royalRumbleSearchRequest.getMinimumAmount()).lte(royalRumbleSearchRequest.getMaximumAmount())
                    .and("competitorLimit")
                    .gte(royalRumbleSearchRequest.getMinimumAmount()).lte(royalRumbleSearchRequest.getMaximumPlayers())
            );
            query.with(Sort.by(royalRumbleSearchRequest.isAscending() ? Sort.Direction.ASC : Sort.Direction.DESC,sortField));
            query.with(pageable);
            List<Match> matches = mongoTemplate.find(query,Match.class);
            return PageableExecutionUtils.getPage(
                    matches,
                    pageable,
                    () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Match.class));
        }
    }

    @Override
    public ArrayList<RoyalRumbleSearchResponse.FormattedMatch> tagRegisteredMatch(ArrayList<String> pendingMatch, ArrayList<RoyalRumbleSearchResponse.FormattedMatch> matches) {
        for(int index = 0 ; index < matches.size(); index++){
            final int tempIndex = index;
            Optional<String> OptionalId = pendingMatch.stream().filter(pm -> pm.equals(matches.get(tempIndex).getId())).findFirst();
            matches.get(tempIndex).setRegistered(OptionalId.isPresent());
        }
        return matches;
    }

    @Override
    public ArrayList<RoyalRumbleSearchResponse.FormattedMatch> tagRegisteredMatchFromStringArray(ArrayList<String> pendingMatchIds, ArrayList<RoyalRumbleSearchResponse.FormattedMatch> matches) {
        return null;
    }

    @Override
    public void endRoyalRumbleMatchesCronJob() {

        //todo sort by date created

        List<Match> matches = matchRepository.getAllByMatchTypeAndMatchState(Enum.MatchType.RoyalRumble, Enum.MatchState.Open);

        for(int index = 0; index < matches.size(); index++){

            Match presentMatch = matches.get(index);

            long startTime = presentMatch.getStartTime();

            long presentTime = new Date().getTime();

            if((presentTime - startTime) / 1000 <= royalRumbleTimeLimitSeconds){

                presentMatch.setMatchState(Enum.MatchState.Close);

                presentMatch.setMatchStatus(Enum.MatchStatus.Ended);

                Optional<Match> matchOptional = matchRepository.findById(presentMatch.getId());

                if(matchOptional.isPresent()){

                    presentMatch = matchOptional.get();

                    presentMatch.setMatchState(Enum.MatchState.Close);

                    presentMatch.setMatchStatus(Enum.MatchStatus.Ended);

                    presentMatch = update(presentMatch);

                    presentMatch.setMatchSeat(matchSeatService.givePosition(presentMatch.getMatchSeat()));

                }
            }
        }
    }
}
