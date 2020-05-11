package com.blue.tnb.controller;

import com.blue.tnb.dto.TicketDTO;
import com.blue.tnb.exception.TicketNotFoundException;
import com.blue.tnb.exception.TicketWithoutUserException;
import com.blue.tnb.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @GetMapping("/findAll")
    public List<TicketDTO> findAllTickets(){
        return ticketService.getAllTickets();
    }
    @GetMapping("/findByUserId/{userId}")
    public TicketDTO findTicketByUserId(@PathVariable Long userId) throws TicketWithoutUserException {

        return ticketService.getTicketByUserId(userId);
    }

    @GetMapping("/findTicket/{id}")
    public ResponseEntity<TicketDTO> findTicketById(@PathVariable Long id) throws TicketNotFoundException {
        if(ticketService.getTicketById(id)!=null)
            return ResponseEntity.ok(ticketService.getTicketById(id));
        else return ResponseEntity.notFound().build();
    }



}
