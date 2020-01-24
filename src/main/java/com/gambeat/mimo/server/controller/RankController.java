package com.gambeat.mimo.server.controller;


import com.gambeat.mimo.server.model.Rank;
import com.gambeat.mimo.server.model.User;
import com.gambeat.mimo.server.model.request.HighScoreRequest;
import com.gambeat.mimo.server.model.response.LeaderBoardResponse;
import com.gambeat.mimo.server.model.response.ResponseModel;
import com.gambeat.mimo.server.service.RankService;
import com.gambeat.mimo.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/leader-board")
public class RankController {

    @Autowired
    RankService rankService;

    @Autowired
    UserService userService;

    @GetMapping(produces = "application/json")
    public @ResponseBody
      ResponseEntity<LeaderBoardResponse> getLeaderBoard() {
        List<Rank> ranks = rankService.getTopTwentyPlayers();
        Rank userRank = rankService.getUserRank(new User());
        LeaderBoardResponse leaderBoardResponse = new LeaderBoardResponse(ranks, userRank);
        return new ResponseEntity<>(leaderBoardResponse, HttpStatus.OK);
    }



    @PostMapping(produces = "application/json")
    public @ResponseBody
    ResponseEntity<ResponseModel> saveHighScore(@RequestBody HighScoreRequest highScoreRequest) {


        User foundUser = userService.getUserByEmail("email");

        if(foundUser.getStatistics().getHighestScore() < highScoreRequest.getScore()){
            long newHighScore = highScoreRequest.getScore();
            foundUser.getStatistics().setHighestScore(newHighScore);
            userService.update(foundUser);
        }

        Rank foundUserRank = rankService.getUserRank(foundUser);

        if("if rank is not found" == ""){
            Rank newRank = new Rank();
            newRank.setUser(foundUser);
            newRank.setPosition(0);
            newRank.setScore(highScoreRequest.getScore());

        }else if(foundUserRank.getScore() < highScoreRequest.getScore()){
            long newHighScore = highScoreRequest.getScore();
            foundUserRank.setScore(newHighScore);
            rankService.update(foundUserRank);
        }

        ResponseModel responseModel = new ResponseModel();
        responseModel.setSuccessful(true);
        responseModel.setMessage("High score has been Successfully Saved");
        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }
}
