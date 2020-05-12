package com.blue.tnb.controller;

import com.blue.tnb.dto.BookResponse;
import com.blue.tnb.dto.TicketDTO;
import com.blue.tnb.exception.TicketExceptions.TicketNotFoundException;
import com.blue.tnb.exception.TicketExceptions.TicketWithoutUserException;
import com.blue.tnb.model.Ticket;
import com.blue.tnb.service.TicketService;
import com.blue.tnb.validator.TicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private TicketValidator ticketValidator;

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
    @GetMapping("/play/{id}/availableTickets")
    public ResponseEntity<Long> getAllAvailableTickets(@PathVariable Long id){
        return ResponseEntity.ok(ticketService.countAvailableTicketsByPlayId(id));
    }
    @GetMapping("/user/{id}/history")
    public ResponseEntity<List<TicketDTO>> getAllTicketsByUserId(@PathVariable Long id){
        return ResponseEntity.ok(ticketService.findAllTicketsByUserId(id));
    }

    @PostMapping("/play/{playId}/book/{userId}")
    public ResponseEntity<BookResponse> bookTicket(@PathVariable(value = "playId") Long playId,
                                                   @PathVariable(value = "userId") Long userId){

        return ResponseEntity.ok(ticketService.bookTicket(playId,userId));
    }

 // /play/{playId}/book/{userId} -> booking pentru un bilet
    // Verificare peste 30 zile(conditie)
    //else eroare cu returnare nr zile ramase pana la urmatorul book

    // pick-up user + ticket
    //Get all plays-verificare pentru playurile active(de azi, in viitor)
    //              -si care au numarul de tickete free cel putin 1
    // de adaugat availableTicketsCount in PlayDTO a. i. sa trimit in front numarul de tichete free pentru fiecare piesa
}
