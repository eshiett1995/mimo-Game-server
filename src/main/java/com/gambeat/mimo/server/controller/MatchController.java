package com.gambeat.mimo.server.controller;


import com.gambeat.mimo.server.model.Enum;
import com.gambeat.mimo.server.model.Match;
import com.gambeat.mimo.server.model.MatchSeat;
import com.gambeat.mimo.server.model.User;
import com.gambeat.mimo.server.model.request.MatchEntryRequest;
import com.gambeat.mimo.server.model.request.RoyalRumbleSearchRequest;
import com.gambeat.mimo.server.model.response.MatchEntryResponse;
import com.gambeat.mimo.server.model.response.RoyalRumbleSearchResponse;
import com.gambeat.mimo.server.service.JwtService;
import com.gambeat.mimo.server.service.MatchService;
import com.gambeat.mimo.server.service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@RequestMapping("/match")
public class MatchController {

    @Autowired
    JwtService jwtService;

    @Autowired
    UserService userService;

    @Autowired
    MatchService matchService;

  @GetMapping(value = "/", produces = "application/json")
    public @ResponseBody ResponseEntity<MatchEntryResponse> getFirstMatch(HttpServletRequest request) {
      if(request.getHeader("Authorization") == null) {
          return new ResponseEntity<>(new MatchEntryResponse(false, "User not authorized"), HttpStatus.OK);
      }
      try {
          Claims claims = jwtService.decodeToken(request.getHeader("Authorization"));
          Optional<User> optionalUser = userService.getUserByEmail((String) claims.get("email"));
          if(!optionalUser.isPresent()) return new ResponseEntity<>(new MatchEntryResponse(false, "User not found"), HttpStatus.OK);
          if(optionalUser.get().getPendingMatch().size() > 0) {
              return new ResponseEntity<>(new MatchEntryResponse(optionalUser.get().getPendingMatch().get(0)), HttpStatus.OK);
          }else{
              return new ResponseEntity<>(new MatchEntryResponse(false, "You have no existing match"), HttpStatus.OK);
          }
      }catch (Exception exception) {
          return new ResponseEntity<>(new MatchEntryResponse(true, "Error fetching existing match"), HttpStatus.OK);
      }
  }


  //TODO submit score route

    @PostMapping(value = "/royal-rumble/submit", produces = "application/json")
    public @ResponseBody
    ResponseEntity<MatchEntryResponse> uploadRoyalRumbleScore(@RequestBody MatchEntryRequest matchEntryRequest, HttpServletRequest request) {
        if(request.getHeader("Authorization") == null) {
            return new ResponseEntity<>(new MatchEntryResponse(false, "User not authorized"), HttpStatus.OK);
        }

        try{
            Claims claims = jwtService.decodeToken(request.getHeader("Authorization"));
            Optional<User> optionalUser = userService.getUserByEmail((String) claims.get("email"));

            //todo collect money

            //todo stage generation algorithm

            //todo debit gambeat fee

            if(!optionalUser.isPresent()) return new ResponseEntity<>(new MatchEntryResponse(false, "User not found"), HttpStatus.OK);

            if(matchService.isBelowMinimumAmount(matchEntryRequest.getEntryFee())){
                return new ResponseEntity<>(new MatchEntryResponse(false, "Your are below the minimum amount"), HttpStatus.OK);
            }

            Match savedMatch = matchService.createRoyalRumbleMatch(optionalUser.get(), matchEntryRequest.getEntryFee());
            optionalUser.get().getPendingMatch().add(savedMatch);
            userService.save(optionalUser.get());
            return new ResponseEntity<>(new MatchEntryResponse(true, "You have entered a match"), HttpStatus.OK);

        }catch (Exception exception){
            return new ResponseEntity<>(new MatchEntryResponse(false, "Error occurred entering a match"), HttpStatus.OK);
        }
    }



    @PostMapping(value = "/royal-rumble/create", produces = "application/json")
    public @ResponseBody
    ResponseEntity<MatchEntryResponse> saveEvent(@RequestBody MatchEntryRequest matchEntryRequest, HttpServletRequest request) {
        if(request.getHeader("Authorization") == null) {
            return new ResponseEntity<>(new MatchEntryResponse(false, "User not authorized"), HttpStatus.OK);
        }

        try{
            Claims claims = jwtService.decodeToken(request.getHeader("Authorization"));
            Optional<User> optionalUser = userService.getUserByEmail((String) claims.get("email"));

            //todo collect money

            //todo stage generation algorithm

            //todo debit gambeat fee

            if(!optionalUser.isPresent()) return new ResponseEntity<>(new MatchEntryResponse(false, "User not found"), HttpStatus.OK);

            if(matchService.isBelowMinimumAmount(matchEntryRequest.getEntryFee())){
                return new ResponseEntity<>(new MatchEntryResponse(false, "Your are below the minimum amount"), HttpStatus.OK);
            }

            Match savedMatch = matchService.createRoyalRumbleMatch(optionalUser.get(), matchEntryRequest.getEntryFee());
            optionalUser.get().getPendingMatch().add(savedMatch);
            userService.save(optionalUser.get());
            return new ResponseEntity<>(new MatchEntryResponse(true, "You have entered a match"), HttpStatus.OK);

        }catch (Exception exception){
            return new ResponseEntity<>(new MatchEntryResponse(false, "Error occurred entering a match"), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/royal-rumble/join", produces = "application/json")
    public @ResponseBody
    ResponseEntity<MatchEntryResponse> joinRoyalRumble(@RequestBody MatchEntryRequest matchEntryRequest, HttpServletRequest request) {
        if(request.getHeader("Authorization") == null) {
            return new ResponseEntity<>(new MatchEntryResponse(false, "User not authorized"), HttpStatus.OK);
        }

        try{
            Claims claims = jwtService.decodeToken(request.getHeader("Authorization"));
            Optional<User> optionalUser = userService.getUserByEmail((String) claims.get("email"));

            //todo collect money

            //todo stage generation algorithm

            //todo debit gambeat fee

            //todo check if user is already in the competition

            if(!optionalUser.isPresent()) return new ResponseEntity<>(new MatchEntryResponse(false, "User not found"), HttpStatus.OK);

            Optional<Match> optionalMatch = matchService.findById(matchEntryRequest.getMatchID());
            if(!optionalMatch.isPresent()) return new ResponseEntity<>(new MatchEntryResponse(false, "Match not found"), HttpStatus.OK);

            if(optionalUser.get().getWallet().getBalance() < optionalMatch.get().getEntryFee()){
                return new ResponseEntity<>(new MatchEntryResponse(false, "You have an insufficient amount in your wallet"), HttpStatus.OK);
            }

            Match match = optionalMatch.get();
            MatchSeat matchSeat = new MatchSeat();
            if(match.getMatchSeat().size() < 2) match.setMatchStatus(Enum.MatchStatus.Started);
            match.getMatchSeat().add(matchSeat);
            matchService.update(match);
            return new ResponseEntity<>(new MatchEntryResponse(true, "You have entered a match"), HttpStatus.OK);
        }catch (Exception exception){
            return new ResponseEntity<>(new MatchEntryResponse(false, "Error occurred entering a match"), HttpStatus.OK);
        }
    }


    @PostMapping(value = "/royal-rumble/search", produces = "application/json")
    public @ResponseBody
    ResponseEntity<RoyalRumbleSearchResponse> getRoyalRumbleMatches(@RequestBody RoyalRumbleSearchRequest royalRumbleSearchRequest, HttpServletRequest request) {
        if(request.getHeader("Authorization") == null) {
            return new ResponseEntity<>(new RoyalRumbleSearchResponse(false, "User not authorized"), HttpStatus.OK);
        }
        try{
            Claims claims = jwtService.decodeToken(request.getHeader("Authorization"));
            Optional<User> optionalUser = userService.getUserByEmail((String) claims.get("email"));
            Page<Match> matchPage = matchService.getActiveRoyalRumbleMatches(PageRequest.of(0,20));
            return new ResponseEntity<>(new RoyalRumbleSearchResponse(matchPage), HttpStatus.OK);
        }catch (Exception exception){
            return new ResponseEntity<>(new RoyalRumbleSearchResponse(false, "Error occurred while fetching matches"), HttpStatus.OK);
        }
    }
}
