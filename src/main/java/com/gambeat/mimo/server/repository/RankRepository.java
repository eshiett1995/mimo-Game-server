package com.gambeat.mimo.server.repository;

import com.gambeat.mimo.server.model.Rank;
import com.gambeat.mimo.server.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RankRepository extends MongoRepository<Rank, String> {

    List<Rank> findFirst20ByScoreDesc();

    Rank findByUser(User user);
}
