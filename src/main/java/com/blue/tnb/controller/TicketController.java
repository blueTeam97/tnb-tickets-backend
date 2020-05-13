package com.blue.tnb.controller;

import com.blue.tnb.dto.BookResponse;
import com.blue.tnb.dto.TicketDTO;
import com.blue.tnb.exception.TicketExceptions.TicketNotFoundException;
import com.blue.tnb.exception.TicketExceptions.TicketWithoutUserException;
import com.blue.tnb.model.Ticket;
import com.blue.tnb.model.User;
import com.blue.tnb.service.TicketService;
import com.blue.tnb.service.TicketServiceImpl;
import com.blue.tnb.validator.TicketValidator;
import com.hazelcast.core.HazelcastInstance;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.LockModeType;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
public class TicketController {

    @Autowired
    private TicketServiceImpl ticketService;

    @Autowired
    private TicketValidator ticketValidator;

    @Autowired
    HazelcastInstance hazelcastInstance;


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
    @GetMapping("/findTicketByPlayId/{playId}")
    public List<TicketDTO> getAllByPlayId(@PathVariable Long playId) throws TicketNotFoundException {
        return ticketService.getAllByPlayId(playId);
    }

    @PostMapping("/addTicket")
    public ResponseEntity<Ticket> addTicket(@RequestBody TicketDTO ticketDTO) throws ParseException {
        System.out.println(ticketDTO);
        if(ticketValidator.validateTicket(ticketDTO)){
            return ResponseEntity.ok(ticketService.addTicket(ticketDTO));
        }
        else return ResponseEntity.badRequest() .build();
    }
    @PutMapping("/updateTicket/{id}")
    public ResponseEntity<Ticket> updateTicket(@RequestBody TicketDTO ticketDTO) throws ParseException {
        System.out.println(ticketDTO);
        if(ticketValidator.validateTicketForUpdate(ticketDTO)){
            Ticket ticket =ticketService.addTicket(ticketDTO);
            return ResponseEntity.ok(ticket);
        }
        else return ResponseEntity.badRequest().build();
    }
    @DeleteMapping("/deleteTicket/{id}")
    public ResponseEntity<Ticket> deleteTicket(@PathVariable Long id){
        if(ticketValidator.validateTicketIdForUpdate(id)){
            return ResponseEntity.ok(ticketService.deleteTicket(id));
        }
        else return ResponseEntity.badRequest().build();
    }
    @GetMapping("/play/{id}/findAllAvailableTickets")
    public ResponseEntity<List<TicketDTO>> findAllAvailableTickets(@PathVariable("id") Long id){
        return ResponseEntity.ok(ticketService.findAllAvailableTicketsByPlayId(id));
    }
    @GetMapping("/play/{id}/availableTickets")
    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    public ResponseEntity<Long> getAllAvailableTickets(@PathVariable Long id){
        return ResponseEntity.ok(ticketService.countAvailableTicketsByPlayId(id));
    }
    @GetMapping("/user/{id}/history")
    public ResponseEntity<List<TicketDTO>> getAllTicketsByUserId(@PathVariable Long id){
        return ResponseEntity.ok(ticketService.findAllTicketsByUserId(id));
    }

    @GetMapping("/play/{playId}/book")
    public ResponseEntity<BookResponse> bookTicket(@PathVariable(value = "playId") Long playId,
                                                   @RequestHeader(value="authorization") String headers){

        return ResponseEntity.ok(ticketService.bookTicket(playId,headers));
    }

 // /play/{playId}/book/{userId} -> booking pentru un bilet
    // Verificare peste 30 zile(conditie)
    //else eroare cu returnare nr zile ramase pana la urmatorul book

    // pick-up user + ticket
    //Get all plays-verificare pentru playurile active(de azi, in viitor)
    //              -si care au numarul de tickete free cel putin 1
    // de adaugat availableTicketsCount in PlayDTO a. i. sa trimit in front numarul de tichete free pentru fiecare piesa
}
