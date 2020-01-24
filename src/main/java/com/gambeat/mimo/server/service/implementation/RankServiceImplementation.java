package com.gambeat.mimo.server.service.implementation;

import com.gambeat.mimo.server.model.Rank;
import com.gambeat.mimo.server.model.User;
import com.gambeat.mimo.server.repository.RankRepository;
import com.gambeat.mimo.server.service.RankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankServiceImplementation implements RankService {

    @Autowired
    RankRepository rankRepository;

    @Override
    public Rank save(Rank rank) {
        return rankRepository.save(rank);
    }

    @Override
    public Rank update(Rank rank) {
        return rankRepository.save(rank);
    }

    @Override
    public List<Rank> getTopTwentyPlayers() {
        return rankRepository.findRankByHighestScore(20);
    }

    @Override
    public Rank getUserRank(User user) {
        return rankRepository.findByUser(user);
    }
}
