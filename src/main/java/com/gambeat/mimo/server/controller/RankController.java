package com.gambeat.mimo.server.controller;


import com.gambeat.mimo.server.model.Rank;
import com.gambeat.mimo.server.model.User;
import com.gambeat.mimo.server.model.response.LeaderBoardResponse;
import com.gambeat.mimo.server.service.RankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/leader-board")
public class RankController {

    @Autowired
    RankService rankService;

    @GetMapping(produces = "application/json")
    public @ResponseBody
      ResponseEntity<LeaderBoardResponse> getLeaderBoard() {
        List<Rank> ranks = rankService.getTopTwentyPlayers();
        Rank userRank = rankService.getUserRank(new User());
        LeaderBoardResponse leaderBoardResponse = new LeaderBoardResponse(ranks, userRank);
        return new ResponseEntity<>(leaderBoardResponse, HttpStatus.OK);
    }
}
