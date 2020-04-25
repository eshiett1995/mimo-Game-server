package com.gambeat.mimo.server.controller;


import com.gambeat.mimo.server.model.*;
import com.gambeat.mimo.server.model.Enum;
import com.gambeat.mimo.server.model.request.MatchCreationRequest;
import com.gambeat.mimo.server.model.request.MatchEntryRequest;
import com.gambeat.mimo.server.model.request.MatchPlayedRequest;
import com.gambeat.mimo.server.model.request.RoyalRumbleSearchRequest;
import com.gambeat.mimo.server.model.response.MatchEntryResponse;
import com.gambeat.mimo.server.model.response.ResponseModel;
import com.gambeat.mimo.server.model.response.RoyalRumbleSearchResponse;
import com.gambeat.mimo.server.service.*;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
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

    @Autowired
    WalletService walletService;

    @Autowired
    StageGeneratorService stageGeneratorService;

    @Autowired TransactionService transactionService;

    @Value("${gambeat.fee}")
    private long gambeatFee;

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

            optionalUser.get().getPendingMatch().add(savedMatch);

            boolean debitSuccessful = walletService.debit(optionalUser.get().getWallet(), matchCreationRequest.getEntryFee() + gambeatFee);

            transactionService.saveEntryFeeTransaction(optionalUser.get().getWallet(), matchCreationRequest.getEntryFee());

            transactionService.saveGambeatFeeTransaction(optionalUser.get().getWallet(), gambeatFee);

            //todo check if wallets gets updated
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

            //todo debit gambeat fee

            //todo check if user is already in the competition

            if(!optionalUser.isPresent()) return new ResponseEntity<>(new MatchEntryResponse(false, "User not found"), HttpStatus.OK);

            Optional<Match> optionalMatch = matchService.findById(matchEntryRequest.getMatchID());

            if(!optionalMatch.isPresent()) return new ResponseEntity<>(new MatchEntryResponse(false, "Match not found"), HttpStatus.OK);

            if(optionalUser.get().getWallet().getBalance() < optionalMatch.get().getEntryFee()){
                return new ResponseEntity<>(new MatchEntryResponse(false, "You have an insufficient amount in your wallet"), HttpStatus.OK);
            }

            Match match = optionalMatch.get();

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
        if(request.getHeader("Authorization") == null) {

            return new ResponseEntity<>(new RoyalRumbleSearchResponse(false, "User not authorized"), HttpStatus.OK);
        }

        try{
            Claims claims = jwtService.decodeToken(request.getHeader("Authorization"));

            Optional<User> optionalUser = userService.getUserByEmail((String) claims.get("email"));

            Page<Match> matchPage = matchService.getActiveRoyalRumbleMatches(PageRequest.of(page,20), royalRumbleSearchRequest);

            return new ResponseEntity<>(new RoyalRumbleSearchResponse(matchPage), HttpStatus.OK);

        }catch (Exception exception){

            return new ResponseEntity<>(new RoyalRumbleSearchResponse(false, "Error occurred while fetching matches"), HttpStatus.OK);

        }
    }
}
