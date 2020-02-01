package com.gambeat.mimo.server.service;


import com.gambeat.mimo.server.model.Rank;
import com.gambeat.mimo.server.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface RankService {

    Rank save(Rank rank);

    Rank update(Rank rank);

    List<Rank> getTopTwentyPlayers();

    Optional<Rank> getUserRank(User user);


}
