package com.gambeat.mimo.server.service;

import com.gambeat.mimo.server.model.Match;
import com.gambeat.mimo.server.model.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface MatchService {
    Optional<Match> findById(String id);
    Match save(Match match);
    Match update(Match match);
    Boolean isBelowMinimumAmount(long entryFee);
    Boolean hasPendingMatch(User user);
    Optional<Match> getPlayableRoyalRumbleMatch(long entryFee);
    Match createRoyalRumbleMatch(User user, long entryFee);
}
