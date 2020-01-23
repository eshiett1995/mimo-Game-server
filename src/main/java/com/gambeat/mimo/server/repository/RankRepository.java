package com.gambeat.mimo.server.repository;

import com.gambeat.mimo.server.model.Rank;
import com.gambeat.mimo.server.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RankRepository extends MongoRepository<RankRepository, String> {

    List<Rank> findFirst20ByScoreDesc();

    Rank findByUser(User user);
}
