package com.gambeat.mimo.server.service;


import com.gambeat.mimo.server.model.Rank;
import com.gambeat.mimo.server.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RankService {

    Rank save(Rank rank);

    Rank update(Rank rank);

    List<Rank> getTopTwentyPlayers();

    Rank getUserRank(User user);


}
