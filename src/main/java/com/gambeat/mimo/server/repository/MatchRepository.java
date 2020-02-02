package com.gambeat.mimo.server.repository;

import com.gambeat.mimo.server.model.Enum;
import com.gambeat.mimo.server.model.Match;
import com.gambeat.mimo.server.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MatchRepository extends MongoRepository<Match, String> {

    Optional<Match> findByMatchSeatUserAndMatchStatus(User user, Enum.MatchStatus matchStatus);

    Optional<Match> findMatchByMatchTypeAndEntryFeeGreaterThanAndAndMatchStatus(Enum.MatchType matchType, long entryFee, Enum.MatchStatus matchStatus);

}
