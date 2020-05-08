package com.blue.tnb.controller;

import com.blue.tnb.dto.PlayDTO;
import com.blue.tnb.exception.PlayNotFoundException;
import com.blue.tnb.service.PlayService;
import com.blue.tnb.validator.TicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class PlayController {

    @Autowired
    private PlayService playService;


    @GetMapping("/findAllPlays")
    public List<PlayDTO> getAll(){
        return playService.getAllPlays();
    }

    @GetMapping("/findPlayById/{id}")
    public ResponseEntity<PlayDTO> getById(@PathVariable(value = "id") Long id) throws PlayNotFoundException {
        return ResponseEntity.ok(playService.getPlayById(id));
    }

    @GetMapping("/findPlayByName/playName")
    public ResponseEntity<PlayDTO> getPlayByName(@RequestParam String playName) throws PlayNotFoundException {
        return ResponseEntity.ok(playService.getPlayByName(playName));
    }

}
