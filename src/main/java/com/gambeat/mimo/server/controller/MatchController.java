package com.gambeat.mimo.server.controller;


import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/match")
public class MatchController {

   /** @GetMapping(value = "/{id}", produces = "application/json")
    public @ResponseBody
    ResponseEntity<Event> getEvent(@PathVariable int id) {
        Event event = eventService.getEvent(id);
        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    **/

   /**
    @PostMapping(value = "/save", produces = "application/json")
    public @ResponseBody
    ResponseEntity<ResponseModel> saveEvent(@RequestBody Event event, HttpServletRequest request) {
        ResponseModel responseModel = new ResponseModel();
        eventService.saveEvent(event);
        responseModel.setSuccessful(true);
        responseModel.setResponseMessage("Event Successfully Saved");
        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }

    @PostMapping(value = "/", produces = "application/json")
    public @ResponseBody
    ResponseEntity<Page<Event>> getEvents(@RequestBody Event event) {
        ResponseModel responseModel = new ResponseModel();
        Page<Event> eventPages = eventService.searchEvents(event);
        return new ResponseEntity<>(eventPages, HttpStatus.OK);
    }**/

}
