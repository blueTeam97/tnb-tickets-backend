package com.blue.tnb.controller;

import com.blue.tnb.dto.BookResponse;
import com.blue.tnb.dto.TicketDTO;
import com.blue.tnb.exception.InvalidDateException;
import com.blue.tnb.exception.TicketExceptions.InvalidTicketForUpdate;
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
@CrossOrigin(origins = "*", maxAge = 3600)
public class TicketController {

    @Autowired
    private TicketServiceImpl ticketService;

    @Autowired
    private TicketValidator ticketValidator;

    @GetMapping("/findAll")
    public List<TicketDTO> findAllTickets() {
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
    @GetMapping("/findTicketsByPlayId/{playId}")
    public List<TicketDTO> getAllByPlayId(@PathVariable Long playId) throws TicketNotFoundException {
        return ticketService.getAllByPlayId(playId);
    }


    @PostMapping("/addTicket")
    public Ticket addTicket(@RequestBody TicketDTO ticketDTO) throws ParseException, InvalidDateException {
        System.out.println(ticketDTO);
        if(ticketValidator.validateTicket(ticketDTO)){
            return ticketService.addTicket(ticketDTO);
        }
        else throw new InvalidDateException();
    }
    @PutMapping("/updateTicket/{id}")
    public TicketDTO updateTicket(@PathVariable("id") Long id,
                                               @RequestBody TicketDTO ticketDTO) throws ParseException, InvalidTicketForUpdate {
        if(ticketValidator.validateTicketForUpdate(ticketDTO)){
            TicketDTO ticket =new TicketDTO(ticketService.updateTicket(id,ticketDTO));
            return ticket;
        }
        else throw new InvalidTicketForUpdate();
    }
    @DeleteMapping("/deleteTicket/{id}")
    public ResponseEntity<TicketDTO> deleteTicket(@PathVariable Long id) throws InvalidTicketForUpdate{
        if(ticketValidator.validateTicketIdForUpdate(id)){
            TicketDTO deletedTicket=new TicketDTO(ticketService.deleteTicket(id));
            if(deletedTicket!=null){
                return ResponseEntity.ok(deletedTicket);
            }
            else return ResponseEntity.badRequest().build();
        }
        else throw new InvalidTicketForUpdate();
    }
    @GetMapping("/play/{id}/findAllAvailableTickets")
    public ResponseEntity<List<TicketDTO>> findAllAvailableTickets(@PathVariable("id") Long id){
        return ResponseEntity.ok(ticketService.findAllAvailableTicketsByPlayId(id));
    }
    @GetMapping("/play/{id}/availableTickets")
    public ResponseEntity<Long> getAllAvailableTickets(@PathVariable Long id){
        return ResponseEntity.ok(ticketService.countAvailableTicketsByPlayId(id));
    }
    @GetMapping("/user/{id}/history")
    public ResponseEntity<List<TicketDTO>> getAllTicketsByUserId(@PathVariable Long id){
        return ResponseEntity.ok(ticketService.findAllTicketsByUserId(id));
    }

    @PostMapping("/play/{playId}/book")
    public ResponseEntity<BookResponse> bookTicket(@PathVariable(value = "playId") Long playId,
                                                   @RequestHeader(value = "authorization") String header){
        return ResponseEntity.ok(ticketService.bookTicket(playId,header));
    }
    @GetMapping("/play/{playId}/book/{userId}")
    public ResponseEntity<BookResponse> bookTicketTest(@PathVariable(value = "playId") Long playId,
                                                        @PathVariable("userId") Long userId){
        return ticketService.tryBookTicketByPlayIdTest(playId,userId);
    }

    @GetMapping("/pickup/{ticketId}&{userId}")
    public ResponseEntity pickUpTicket(@PathVariable(value = "ticketId") Long ticketId,
                                       @PathVariable(value = "userId") Long userId){
        boolean result=ticketService.pickUpTicketByUserAndTicketId(ticketId,userId);
        if(result){
            return ResponseEntity.ok().build();
        }
        else return ResponseEntity.badRequest().build();
    }

}
