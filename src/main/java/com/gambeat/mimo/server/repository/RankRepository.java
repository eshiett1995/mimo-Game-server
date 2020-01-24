package com.gambeat.mimo.server.repository;

import com.gambeat.mimo.server.model.Rank;
import com.gambeat.mimo.server.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RankRepository extends MongoRepository<Rank, String> {

    @Query("sort: {score:-1}, limit: ?0 }")
    List<Rank> findRankByHighestScore(int limit);

    Rank findByUser(User user);
}
