package com.blue.tnb.controller;

import com.blue.tnb.dto.PlayDTO;
import com.blue.tnb.model.Play;
import com.blue.tnb.service.PlayService;
import com.blue.tnb.validator.PlayValidator;
import java.text.ParseException;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/v1"})
public class PlayController {
    @Autowired
    private PlayService playService;
    @Autowired
    private PlayValidator playValidator;

    public PlayController() {
    }

    @GetMapping({"/findAll"})
    public ResponseEntity<List<PlayDTO>> getAll() {
        return this.playService.getAllPlays() == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(this.playService.getAllPlays());
    }

    @GetMapping({"/findById/{id}"})
    public ResponseEntity<PlayDTO> getById(@PathVariable("id") @NotNull Long id) {
        return ResponseEntity.ok(this.playService.getPlayById(id));
    }

    @GetMapping({"/findByName"})
    public ResponseEntity<PlayDTO> getPlayByName(@RequestParam("playName") String playName) {
        return this.playService.getPlayByName(playName) != null ? ResponseEntity.ok(this.playService.getPlayByName(playName)) : ResponseEntity.notFound().build();
    }

    @PostMapping({"/add"})
    public ResponseEntity<Play> addPlay(@RequestBody PlayDTO playDTO) throws ParseException {
        if (this.playValidator.validateTicketsNumber(playDTO.getTicketsNumber())) {
            Play play = this.playService.addPlay(playDTO);
            System.out.println(play);
            return ResponseEntity.ok(play);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping({"/play/{id}"})
    public ResponseEntity<Play> updatePlay(@PathVariable @NotNull Long id, @RequestBody PlayDTO playDTO) throws ParseException {
        return this.playValidator.validateUpdate(playDTO) ? ResponseEntity.ok(this.playService.updatePlay(playDTO, id)) : ResponseEntity.badRequest().build();
    }

    @DeleteMapping({"/play/{id}"})
    public ResponseEntity<Play> removePlay(@PathVariable("id") @NotNull Long id) {
        return this.playValidator.validateId(id) ? ResponseEntity.ok(this.playService.deletePlay(id)) : ResponseEntity.badRequest().build();
    }
}
