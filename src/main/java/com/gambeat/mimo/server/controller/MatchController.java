package com.gambeat.mimo.server.controller;


import com.gambeat.mimo.server.model.Enum;
import com.gambeat.mimo.server.model.Match;
import com.gambeat.mimo.server.model.MatchSeat;
import com.gambeat.mimo.server.model.User;
import com.gambeat.mimo.server.model.request.MatchEntryRequest;
import com.gambeat.mimo.server.model.response.MatchEntryResponse;
import com.gambeat.mimo.server.service.JwtService;
import com.gambeat.mimo.server.service.MatchService;
import com.gambeat.mimo.server.service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
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



    @PostMapping(value = "/royal-rumble", produces = "application/json")
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

            if(!optionalUser.isPresent()) return new ResponseEntity<>(new MatchEntryResponse(false, "User not found"), HttpStatus.OK);

            if(matchService.isBelowMinimumAmount(matchEntryRequest.getEntryFee())){
                return new ResponseEntity<>(new MatchEntryResponse(false, "Your are below the minimum amount"), HttpStatus.OK);
            }

            if(matchService.hasPendingMatch(optionalUser.get())){
                return new ResponseEntity<>(new MatchEntryResponse(false, "You are in an ongoing match"), HttpStatus.OK);
            }

            if(optionalUser.get().getWallet().getBalance() < matchEntryRequest.getEntryFee()){
                return new ResponseEntity<>(new MatchEntryResponse(false, "You have an insufficient amount in your wallet"), HttpStatus.OK);
            }

            Optional<Match> optionalMatch = matchService.getPlayableRoyalRumbleMatch(matchEntryRequest.getEntryFee());
            if(optionalMatch.isPresent()){
                Match match = optionalMatch.get();
                MatchSeat matchSeat = new MatchSeat();
                if(match.getMatchSeat().size() < 2) match.setMatchStatus(Enum.MatchStatus.Started);
                match.getMatchSeat().add(matchSeat);
                matchService.update(match);
                optionalUser.get().getPendingMatch().add(match);
                userService.save(optionalUser.get());
                return new ResponseEntity<>(new MatchEntryResponse(true, "You have entered a match"), HttpStatus.OK);
            }else{
               Match savedMatch = matchService.createRoyalRumbleMatch(optionalUser.get(), matchEntryRequest.getEntryFee());
               optionalUser.get().getPendingMatch().add(savedMatch);
               userService.save(optionalUser.get());
               return new ResponseEntity<>(new MatchEntryResponse(true, "You have entered a match"), HttpStatus.OK);
            }
        }catch (Exception exception){
            return new ResponseEntity<>(new MatchEntryResponse(false, "Error occurred entering a match"), HttpStatus.OK);
        }
    }

//    @PostMapping(value = "/", produces = "application/json")
//    public @ResponseBody
//    ResponseEntity<Page<Event>> getEvents(@RequestBody Event event) {
//        ResponseModel responseModel = new ResponseModel();
//        Page<Event> eventPages = eventService.searchEvents(event);
//        return new ResponseEntity<>(eventPages, HttpStatus.OK);
//    }

}
