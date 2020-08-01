package com.gambeat.mimo.server.controller;


import com.gambeat.mimo.server.model.*;
import com.gambeat.mimo.server.model.Enum;
import com.gambeat.mimo.server.model.request.MatchCreationRequest;
import com.gambeat.mimo.server.model.request.MatchEntryRequest;
import com.gambeat.mimo.server.model.request.MatchPlayedRequest;
import com.gambeat.mimo.server.model.request.RoyalRumbleSearchRequest;
import com.gambeat.mimo.server.model.response.GameStageResponse;
import com.gambeat.mimo.server.model.response.MatchEntryResponse;
import com.gambeat.mimo.server.model.response.ResponseModel;
import com.gambeat.mimo.server.model.response.RoyalRumbleSearchResponse;
import com.gambeat.mimo.server.service.*;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
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

    final
    JwtService jwtService;

    final
    UserService userService;

    final
    MatchService matchService;

    final
    WalletService walletService;

    final
    GameStageService gameStageService;

    final TransactionService transactionService;

    @Value("${gambeat.fee}")
    private long gambeatFee;

    public MatchController(JwtService jwtService, UserService userService, MatchService matchService, WalletService walletService, GameStageService gameStageService, TransactionService transactionService) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.matchService = matchService;
        this.walletService = walletService;
        this.gameStageService = gameStageService;
        this.transactionService = transactionService;
    }

//  @GetMapping(value = "/", produces = "application/json")
//    public @ResponseBody ResponseEntity<MatchEntryResponse> getFirstMatch(HttpServletRequest request) {
//
//      if(request.getHeader("Authorization") == null) {
//
//          return new ResponseEntity<>(new MatchEntryResponse(false, "User not authorized"), HttpStatus.OK);
//
//      }
//
//      try {
//
//          Claims claims = jwtService.decodeToken(request.getHeader("Authorization"));
//
//          Optional<User> optionalUser = userService.getUserByEmail((String) claims.get("email"));
//
//          if(!optionalUser.isPresent()) return new ResponseEntity<>(new MatchEntryResponse(false, "User not found"), HttpStatus.OK);
//
//          if(optionalUser.get().getPendingMatch().size() > 0) {
//
//              return new ResponseEntity<>(new MatchEntryResponse(optionalUser.get().getPendingMatch().get(0)), HttpStatus.OK);
//
//          }else{
//
//              return new ResponseEntity<>(new MatchEntryResponse(false, "You have no existing match"), HttpStatus.OK);
//
//          }
//
//      }catch (Exception exception) {
//
//          return new ResponseEntity<>(new MatchEntryResponse(true, "Error fetching existing match"), HttpStatus.OK);
//      }
//  }


    @PostMapping(value = "/royal-rumble/submit", produces = "application/json")
    public @ResponseBody
    ResponseEntity<ResponseModel> uploadRoyalRumbleScore(@RequestBody MatchPlayedRequest matchPlayedRequest, HttpServletRequest request) {
      if(request.getHeader("Authorization") == null) {
            return new ResponseEntity<>(new MatchEntryResponse(false, "User not authorized"), HttpStatus.OK);
      }

      try{
            Claims claims = jwtService.decodeToken(request.getHeader("Authorization"));
            Optional<User> optionalUser = userService.getUserByEmail((String) claims.get("email"));
            if(!optionalUser.isPresent()) return new ResponseEntity<>(new MatchEntryResponse(false, "User not found"), HttpStatus.OK);
            Optional<Match> matchOptional = matchService.findById(matchPlayedRequest.getMatchID());

            if(!matchOptional.isPresent()){
                return new ResponseEntity<>(new ResponseModel(false, "Royal rumble match not found"), HttpStatus.OK);
            }

            Match foundMatch = matchOptional.get();

            if(matchService.hasExceedDuration(foundMatch)){
                return new ResponseEntity<>(new ResponseModel(false, "You are unable to post, this match has closed"), HttpStatus.OK);
            }

            MatchSeat foundMatchSeat;

            Optional<MatchSeat> matchSeatOptional = foundMatch.getMatchSeat().stream().
                    filter(ms -> ms.getUser().equals(optionalUser.get())).findFirst();

          if(!matchSeatOptional.isPresent()){
              return new ResponseEntity<>(new ResponseModel(false, "User's seat not found in the Match"), HttpStatus.OK);

          }else{
              foundMatchSeat = matchSeatOptional.get();
              int index = foundMatch.getMatchSeat().indexOf(foundMatchSeat);
              foundMatchSeat.setPoints(matchPlayedRequest.getScores());
              foundMatch.getMatchSeat().remove(index);
              foundMatch.getMatchSeat().add(index, foundMatchSeat);
              matchService.update(foundMatch);
          }
          return new ResponseEntity<>(new MatchEntryResponse(true, "You have entered a match"), HttpStatus.OK);

        }catch (Exception exception){
            return new ResponseEntity<>(new MatchEntryResponse(false, "Error occurred entering a match"), HttpStatus.OK);
      }

    }


    @PostMapping(value = "/royal-rumble/create", produces = "application/json")
    public @ResponseBody
    ResponseEntity<MatchEntryResponse> saveEvent(@RequestBody MatchCreationRequest matchCreationRequest, HttpServletRequest request) {
        if(request.getHeader("Authorization") == null) {
            return new ResponseEntity<>(new MatchEntryResponse(false, "User not authorized"), HttpStatus.OK);
        }

        try{
            Claims claims = jwtService.decodeToken(request.getHeader("Authorization"));
            Optional<User> optionalUser = userService.getUserByEmail((String) claims.get("email"));

            if(!optionalUser.isPresent()) return new ResponseEntity<>(new MatchEntryResponse(false, "User not found"), HttpStatus.OK);

            if(matchService.isBelowMinimumAmount(matchCreationRequest.getEntryFee())){
                return new ResponseEntity<>(new MatchEntryResponse(false, "Your are below the minimum amount"), HttpStatus.OK);
            }

            if(optionalUser.get().getWallet().getBalance() < matchCreationRequest.getEntryFee()){
                return new ResponseEntity<>(new MatchEntryResponse(false, "Insufficient wallet balance"), HttpStatus.OK);
            }

            Match savedMatch = matchService.createRoyalRumbleMatch(optionalUser.get(), matchCreationRequest);
            optionalUser.get().getPendingMatch().add(savedMatch.getId());
            boolean debitSuccessful = walletService.debit(optionalUser.get().getWallet(), matchCreationRequest.getEntryFee() + gambeatFee);
            transactionService.saveEntryFeeTransaction(optionalUser.get().getWallet(), matchCreationRequest.getEntryFee());
            transactionService.saveGambeatFeeTransaction(optionalUser.get().getWallet(), gambeatFee);

            userService.save(optionalUser.get());

            if(debitSuccessful) {
                return new ResponseEntity<>(new MatchEntryResponse(true, "You have entered a match"), HttpStatus.OK);
            }else{
                matchService.delete(savedMatch);
                return new ResponseEntity<>(new MatchEntryResponse(true, "An error occurred while debiting you"), HttpStatus.OK);
            }

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

            if(!optionalUser.isPresent()) return new ResponseEntity<>(new MatchEntryResponse(false, "User not found"), HttpStatus.OK);

            Optional<Match> optionalMatch = matchService.findById(matchEntryRequest.getMatchID());

            if(!optionalMatch.isPresent()) return new ResponseEntity<>(new MatchEntryResponse(false, "Match not found"), HttpStatus.OK);

            if(matchService.hasUserAlreadyJoined(optionalUser.get(), optionalMatch.get())){
                return new ResponseEntity<>(new MatchEntryResponse(false, "You have already joined this match"), HttpStatus.OK);
            }

            if(optionalUser.get().getWallet().getBalance() < optionalMatch.get().getEntryFee()){
                return new ResponseEntity<>(new MatchEntryResponse(false, "You have an insufficient amount in your wallet"), HttpStatus.OK);
            }
            Match match = optionalMatch.get();
            optionalUser.get().getPendingMatch().add(match.getId());
            userService.update(optionalUser.get());
            /*
              Over here gambeat fee & tournament entry fee
              are added and removed from the users wallet.
            */
            boolean debitSuccessful = walletService.debit(optionalUser.get().getWallet(), match.getEntryFee() + gambeatFee);
            transactionService.saveEntryFeeTransaction(optionalUser.get().getWallet(), match.getEntryFee());
            transactionService.saveGambeatFeeTransaction(optionalUser.get().getWallet(), gambeatFee);

            if(debitSuccessful) {
                MatchSeat matchSeat = new MatchSeat(optionalUser.get());
                if (match.getMatchSeat().size() < 2) match.setMatchStatus(Enum.MatchStatus.Started);
                match.getMatchSeat().add(matchSeat);
                matchService.update(match);
                return new ResponseEntity<>(new MatchEntryResponse(true, "You have entered a match"), HttpStatus.OK);

            }else {
                return new ResponseEntity<>(new MatchEntryResponse(false, "Error occurred Debiting your wallet"), HttpStatus.OK);
            }
        }catch (Exception exception){

            return new ResponseEntity<>(new MatchEntryResponse(false, "Error occurred entering a match"), HttpStatus.OK);

        }
    }


    @PostMapping(value = "/royal-rumble/search/{page}", produces = "application/json")
    public @ResponseBody
    ResponseEntity<RoyalRumbleSearchResponse> getRoyalRumbleMatches(@PathVariable("page") int page,
                                                                    @RequestBody RoyalRumbleSearchRequest royalRumbleSearchRequest,
                                                                    HttpServletRequest request) {

        System.out.println(new Gson().toJson(royalRumbleSearchRequest));
        if(request.getHeader("Authorization") == null) {
            return new ResponseEntity<>(new RoyalRumbleSearchResponse(false, "User not authorized"), HttpStatus.OK);
        }

        try{
            Claims claims = jwtService.decodeToken(request.getHeader("Authorization"));

            Optional<User> optionalUser = userService.getUserByEmail((String) claims.get("email"));
            if(!optionalUser.isPresent())
                return new ResponseEntity<>(new RoyalRumbleSearchResponse(false, "No player found"), HttpStatus.OK);

            Page<Match> matchPage = matchService.getActiveRoyalRumbleMatches(PageRequest.of(page,20), royalRumbleSearchRequest);
            System.out.println(page);
            System.out.println(matchPage.getContent().size());
            RoyalRumbleSearchResponse royalRumbleSearchResponse = new RoyalRumbleSearchResponse(matchPage);
            /*
              this adds registered(true) to matches the user has been registered to.
             */
            royalRumbleSearchResponse.setContent(matchService.tagRegisteredMatch(optionalUser.get().getPendingMatch(), royalRumbleSearchResponse.getContent()));
            royalRumbleSearchResponse.setSuccessful(true);
            royalRumbleSearchResponse.setMessage("Royal rumble match successfully retrieved");
            System.out.println(new Gson().toJson(royalRumbleSearchResponse.getContent()));
            return new ResponseEntity<>(royalRumbleSearchResponse, HttpStatus.OK);

        }catch (Exception exception){
            System.out.println("it reached ana error");
            return new ResponseEntity<>(new RoyalRumbleSearchResponse(false, "Error occurred while fetching matches"), HttpStatus.OK);

        }
    }

    @GetMapping(value = "/get/stage/{match_id}", produces = "application/json")
    public @ResponseBody
    ResponseEntity<GameStageResponse> getRoyalRumbleMatches(@PathVariable("match_id") String match_id) {
        try{
            Optional<GameStage> gameStageOptional = gameStageService.getGameStageByMatchId(match_id);
            if(gameStageOptional.isPresent()){
                GameStage gameStage = gameStageOptional.get();
                GameStageResponse responseModel = new GameStageResponse(true, "Successfully retrieved");
                responseModel.setData(new Gson().toJson(gameStage.getStageObjects()));
                return new ResponseEntity<>(responseModel, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(new GameStageResponse(false, "No stage has been found"), HttpStatus.OK);
            }

        }catch (Exception exception){
            return new ResponseEntity<>(new GameStageResponse(false, "Error occurred while fetching stage"), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/royal-rumble/init/{match_id}", produces = "application/json")
    public @ResponseBody
    ResponseEntity<GameStageResponse> initRoyalRumbleMatch(@PathVariable("match_id") String match_id, HttpServletRequest request) {
        if(request.getHeader("Authorization") == null) {
            return new ResponseEntity<>(new GameStageResponse(false, "User not authorized"), HttpStatus.OK);
        }

        try{
            Claims claims = jwtService.decodeToken(request.getHeader("Authorization"));

            Optional<User> optionalUser = userService.getUserByEmail((String) claims.get("email"));

            if(!optionalUser.isPresent())
                return new ResponseEntity<>(new GameStageResponse(false, "No player found"), HttpStatus.OK);

            Optional<Match> matchOptional = matchService.findById(match_id);
            if(!matchOptional.isPresent())
                return new ResponseEntity<>(new GameStageResponse(false, "No match found"), HttpStatus.OK);


            User foundUser = optionalUser.get();
            Match foundMatch = matchOptional.get();

            System.out.print(foundUser.getId());

            Optional<MatchSeat> matchSeatOptional = foundMatch.getMatchSeat().stream().filter(matchSeat -> matchSeat.getUser().getId().equals(foundUser.getId())).findFirst();

            if(!matchSeatOptional.isPresent())
                return new ResponseEntity<>(new GameStageResponse(false, "You don't have a place in this match"), HttpStatus.OK);

            Optional<GameStage> gameStageOptional = gameStageService.getGameStageByMatchId(match_id);
            if(gameStageOptional.isPresent()){
                GameStage gameStage = gameStageOptional.get();
                //this is temporary ("should be removed after")
                gameStage.getStageObjects().forEach(stageObject -> {
                    stageObject.setItem(stageObject.getObject());
                });
                GameStageResponse responseModel = new GameStageResponse(true, "Successfully retrieved");
                responseModel.setData(new Gson().toJson(gameStage.getStageObjects()));
                return new ResponseEntity<>(responseModel, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(new GameStageResponse(false, "No stage has been found"), HttpStatus.OK);
            }

        }catch (Exception exception){
            return new ResponseEntity<>(new GameStageResponse(false, "Error occurred while fetching stage"), HttpStatus.OK);
        }
    }
}
