package com.gambeat.mimo.server.controller;


import com.gambeat.mimo.server.model.Rank;
import com.gambeat.mimo.server.model.User;
import com.gambeat.mimo.server.model.request.HighScoreRequest;
import com.gambeat.mimo.server.model.response.LeaderBoardResponse;
import com.gambeat.mimo.server.model.response.ResponseModel;
import com.gambeat.mimo.server.service.JwtService;
import com.gambeat.mimo.server.service.RankService;
import com.gambeat.mimo.server.service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/leader-board")
public class RankController {

    final
    RankService rankService;

    final
    UserService userService;

    final
    JwtService jwtService;

    public RankController(RankService rankService, UserService userService, JwtService jwtService) {
        this.rankService = rankService;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping(produces = "application/json")
    public @ResponseBody
      ResponseEntity<LeaderBoardResponse> getLeaderBoard(HttpServletRequest request) {
        LeaderBoardResponse leaderBoardResponse;
        if(request.getHeader("Authorization") == null) {
            return new ResponseEntity<>(new LeaderBoardResponse(false, "User not authorized"), HttpStatus.OK);
        }

        try {
            Claims claims = jwtService.decodeToken(request.getHeader("Authorization"));
            List<Rank> ranks = rankService.getTopTwentyPlayers();
            Optional<User> optionalUser = userService.getUserByEmail((String) claims.get("email"));
            if(!optionalUser.isPresent()) return new ResponseEntity<>(new LeaderBoardResponse(false, "User not found"), HttpStatus.OK);
            Optional<Rank>  optionalRank = rankService.getUserRank(optionalUser.get());
            leaderBoardResponse = new LeaderBoardResponse(ranks, optionalRank.orElse(null));
            leaderBoardResponse.setMessage("Successful");
            leaderBoardResponse.setSuccessful(true);
            return new ResponseEntity<>(leaderBoardResponse, HttpStatus.OK);

        }catch (Exception error){
            return new ResponseEntity<>(new LeaderBoardResponse(false, "Oops! unable to get ranks"), HttpStatus.OK);
        }

    }



    @PostMapping(produces = "application/json")
    public @ResponseBody
    ResponseEntity<ResponseModel> saveHighScore(@RequestBody HighScoreRequest highScoreRequest, HttpServletRequest request) {
        if(request.getHeader("Authorization") == null) {
            return new ResponseEntity<>(new ResponseModel(false, "User not authorized"), HttpStatus.OK);
        }

        //try{
            Claims claims = jwtService.decodeToken(request.getHeader("Authorization"));

            Optional<User> optionalUser = userService.getUserByEmail((String) claims.get("email"));
            if(!optionalUser.isPresent()){
                return new ResponseEntity<>( new ResponseModel(false,"User not found"), HttpStatus.OK);
            }
            User foundUser = optionalUser.get();
            if(foundUser.getStatistics().getHighestScore() < highScoreRequest.getScore()){
                long newHighScore = highScoreRequest.getScore();
                foundUser.getStatistics().setHighestScore(newHighScore);
                userService.update(foundUser);
            }

            Optional<Rank> optionalRank = rankService.getUserRank(foundUser);
            Rank rank;

            if(!optionalRank.isPresent()){
                System.out.println("not present");
                rank = new Rank();
                rank.setUser(foundUser);
                rank.setPosition(0);
                rank.setScore(highScoreRequest.getScore());
                rankService.save(rank);

            }else {
                rank = optionalRank.get();
                System.out.println("is present");
                System.out.println("is present " + rank.getId());
                if(rank.getScore() < highScoreRequest.getScore()) {
                long newHighScore = highScoreRequest.getScore();
                    rank.setScore(newHighScore);
                rankService.update(rank);
              }
            }
            return new ResponseEntity<>( new ResponseModel(true,"High score has been successfully saved"), HttpStatus.OK);
       //}catch (Exception exception){
         //   System.out.println(exception);
           // return new ResponseEntity<>( new ResponseModel(false,"Error occurred saving score"), HttpStatus.OK);
        //}
    }
}
