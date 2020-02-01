package com.gambeat.mimo.server.service.implementation;

import com.gambeat.mimo.server.model.Rank;
import com.gambeat.mimo.server.model.User;
import com.gambeat.mimo.server.repository.RankRepository;
import com.gambeat.mimo.server.service.RankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        return rankRepository.findAll(PageRequest.of(0, 20, Sort.by("score").descending())).getContent();
    }

    @Override
    public Optional<Rank> getUserRank(User user) {
        return rankRepository.findByUser(user);
    }
}
