package com.blue.tnb.controller;

import com.blue.tnb.dto.BookResponse;
import com.blue.tnb.dto.PlayDTO;
import com.blue.tnb.dto.TicketDTO;
import com.blue.tnb.exception.InvalidDateException;
import com.blue.tnb.exception.TicketExceptions.InvalidTicketForUpdate;
import com.blue.tnb.exception.TicketExceptions.TicketNotFoundException;
import com.blue.tnb.exception.TicketExceptions.TicketWithoutUserException;
import com.blue.tnb.model.Ticket;
import com.blue.tnb.model.User;
import com.blue.tnb.repository.UserRepository;
import com.blue.tnb.service.TicketService;
import com.blue.tnb.service.TicketServiceImpl;
import com.blue.tnb.service.UserDetailsServiceImpl;
import com.blue.tnb.validator.TicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TicketController {

    @Autowired
    private TicketServiceImpl ticketService;

    @Autowired
    private TicketValidator ticketValidator;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

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
    @GetMapping("/user/history")
    public ResponseEntity<List<TicketDTO>> getAllTicketsByUserId( @RequestHeader(value = "authorization") String header){
        List<TicketDTO> userHistory=ticketService.findAllTicketsByCurrentUser(header);
        if(userHistory==null && userHistory.size()<=0){
            return ResponseEntity.notFound().build();
        }
        else return ResponseEntity.ok(userHistory);
    }

    @PostMapping("/play/{playId}/book")
    public ResponseEntity<BookResponse> bookTicket(@PathVariable(value = "playId") Long playId,
                                                   @RequestHeader(value = "authorization") String header){
        return ResponseEntity.ok(ticketService.bookTicket(playId,header));
    }
    @GetMapping("/play/{playId}/book/{userId}") //Just for the benchmarking
    public ResponseEntity<BookResponse> bookTicketTest(@PathVariable(value = "playId") Long playId,
                                                        @PathVariable("userId") Long userId){
        return ResponseEntity.ok(ticketService.bookTicketHardWay(playId,userId));
        //return ticketService.tryBookTicketByPlayIdTest(playId,userId);
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

    @PutMapping("/user/changeSubscribe")
    public ResponseEntity<Integer> subscribe(@RequestHeader(value = "authorization") String header){
        Integer ok=userDetailsService.updateSubscribeForUser(header);
        if(ok>=0){
            return ResponseEntity.ok(ok);
        }
        else return ResponseEntity.badRequest().build();
    }

   /* @GetMapping("/play/{id}/findAllBookedTickets")
    public List<TicketForAdminDTO> getBookedTickets(@PathVariable(value = "id") Long id) {
        return ticketService.findAllBookedTicketsByPlayId(id);
    }*/

    @GetMapping("user/{id}/findEmailByUserId")
    public Optional<String> getEmailByUserId(@PathVariable(value = "id") Long id){
        return ticketService.findEmailByUserId(id);
    }

    @GetMapping("/findAllBookedTicketsByPlayId/{id}")
    public List<TicketDTO> getAllBookedTickets(@PathVariable(value = "id") Long id) {
        return ticketService.findAllBookedTicketsByPlayId(id);
    }
    @GetMapping("/user/getSubscribeStatus")
    public ResponseEntity<Boolean> getSubscribeStatus(@RequestHeader(value="authorization") String header){
        Boolean status=ticketService.getStatusForLoggedUser(header);
        return ResponseEntity.ok(status);
    }

    @PutMapping("/user/ticket/{playId}/unbook")
    public ResponseEntity<Boolean> unbookTicket(@RequestHeader(value="authorization") String header,
                                                @PathVariable(value="playId") Long playId){
        Boolean ok = ticketService.unbookTicketforLoggedUser(header,playId);
        return ok?ResponseEntity.ok(true):ResponseEntity.badRequest().build();
    }

}
