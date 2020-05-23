package com.blue.tnb.controller;

import com.blue.tnb.dto.PlayDTO;
import com.blue.tnb.dto.UserPlaysPopulator;
import com.blue.tnb.exception.PlayExceptions.InvalidDateException;
import com.blue.tnb.exception.PlayExceptions.PlayDeleteException;
import com.blue.tnb.exception.PlayExceptions.PlayNotFoundException;
import com.blue.tnb.exception.PlayExceptions.PlayUpdateException;
import com.blue.tnb.exception.TicketExceptions.TicketsNumberException;
import com.blue.tnb.model.Play;
import com.blue.tnb.service.PlayService;
import java.util.List;
import javax.validation.constraints.NotNull;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PlayController {

    @Autowired
    private PlayService playService;

    public PlayController() {}

    @GetMapping("/user/findPlays")
    public ResponseEntity<UserPlaysPopulator> getAllAvailablePlaysForLoggedUser(@RequestHeader("authorization") String header){
        UserPlaysPopulator availablePlays=playService.populateUserWithPlays(header);
        return ResponseEntity.ok(availablePlays);
    }

    @GetMapping({"/findAll"})
    public ResponseEntity<List<PlayDTO>> getAll() {
        return playService.getAllPlays() == null ?
                ResponseEntity.noContent().build() : ResponseEntity.ok(this.playService.getAllPlays());
    }

    @GetMapping({"/findById/{id}"})
    public ResponseEntity<PlayDTO> getById(@PathVariable("id") @NotNull Long id) throws PlayNotFoundException {
        return ResponseEntity.ok(playService.getPlayById(id));
    }

    @GetMapping({"/findByName"})
    public ResponseEntity<PlayDTO> getPlayByName(@RequestParam("playName") String playName) throws PlayNotFoundException {
        return ResponseEntity.ok(playService.getPlayByName(playName));
    }

    @PostMapping({"/add"})
    public ResponseEntity<PlayDTO> addPlay(@RequestBody PlayDTO playDTO) throws InvalidDateException {
        return ResponseEntity.ok(playService.addPlay(playDTO));
    }

    @PutMapping({"/play/{id}"})
    public ResponseEntity<PlayDTO> updatePlay(@PathVariable @NotNull Long id, @RequestBody PlayDTO playDTO) throws PlayUpdateException, InvalidDateException, TicketsNumberException {
        return ResponseEntity.ok(playService.updatePlay(playDTO, id));
    }

    @DeleteMapping({"/play/{id}"})
    public ResponseEntity<PlayDTO> removePlay(@PathVariable("id") @NotNull Long id) throws PlayDeleteException {
        return ResponseEntity.ok(this.playService.deletePlay(id));
    }
    @GetMapping("/user/testBookedPlays")
    public ResponseEntity<List<PlayDTO>> getAllBookedPlays(@RequestHeader(value = "authorization") String header){

        return ResponseEntity.ok(playService.getAllBookedPlaysForLoggedUser(header));
    }
}
