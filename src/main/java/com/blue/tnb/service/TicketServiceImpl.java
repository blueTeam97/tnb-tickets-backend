package com.blue.tnb.service;

import com.blue.tnb.constants.Status;
import com.blue.tnb.dto.BookResponse;
import com.blue.tnb.dto.PlayDTO;
import com.blue.tnb.dto.TicketDTO;
import com.blue.tnb.exception.TicketExceptions.TicketNotFoundException;
import com.blue.tnb.exception.TicketExceptions.TicketWithoutUserException;
import com.blue.tnb.mapper.PlayMapper;
import com.blue.tnb.mapper.TicketMapper;
import com.blue.tnb.model.Ticket;
import com.blue.tnb.repository.PlayRepository;
import com.blue.tnb.repository.TicketRepository;
import com.blue.tnb.repository.UserRepository;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service//Nu mai pot implementa interfata service pentru ca nu ma lasa Async-ul(Crapa)
public class TicketServiceImpl{

    @Autowired
    private PlayMapper playMapper;
    @Autowired
    private PlayRepository playRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HazelcastInstance hazelcastInstance;

    //@Override
    public List<TicketDTO> getAllTickets() {
        return ticketRepository.findAll().stream()
                                .map(ticketMapper::ticketToTicketDTO)
                                .collect(Collectors.toList());
    }

    //@Override
    public TicketDTO getTicketByUserId(Long userId) throws TicketWithoutUserException {
        return ticketRepository.findAllByUserId(userId).stream()
                                .filter(ticket-> ticket.getUserId().equals(userId))
                                .map(ticketMapper::ticketToTicketDTO)
                                .findAny().orElseThrow(()->new TicketWithoutUserException(userId));
    }

   // @Override
    public TicketDTO getTicketById(Long ticketId) throws TicketNotFoundException {
        return ticketMapper.ticketToTicketDTO(ticketRepository.getOne(ticketId));
    }

    //@Override
    public List<TicketDTO> getAllByPlayId(Long id) {
        return ticketMapper.convertTicketToTicketDTOList(ticketRepository.findAllByPlayId(id));
    }

   // @Override
    public Ticket addTicket(TicketDTO ticketDTO) throws ParseException {
        return ticketRepository.saveAndFlush(ticketMapper.ticketDTOToTicket(ticketDTO));
    }

    public Ticket updateTicket(Long id, TicketDTO ticketDTO){
        Ticket ticket=ticketRepository.getOne(id);
        return ticketRepository.saveAndFlush(ticket);
    }

    //@Override
    public Ticket deleteTicket(Long ticketId) {
        Optional<Ticket> existingTicket = ticketRepository.findById(ticketId);
        if (existingTicket.isPresent()) {
            ticketRepository.delete(existingTicket.get());
            return existingTicket.get();
        } else return null;
    }

   // @Override
    public Long countAvailableTicketsByPlayId(Long playId) {
        return ticketRepository.countAllAvailableByPlayId(playId);
    }

  //  @Override
    public List<TicketDTO> findAllTicketsByUserId(Long id) {
        return ticketMapper.convertTicketToTicketDTOList(ticketRepository.findAllByUserId(id));
    }

   // @Override
    public List<TicketDTO> findAllAvailableTicketsByPlayId(Long id) {

       /* return ticketRepository.findAllAvailableByPlayId(id).stream()
                                                            .map(ticketMapper::ticketToTicketDTO)

                                                            .collect(Collectors.toList());*/
        Map<Long,List<Long>> map=hazelcastInstance.getMap("availableTickets");
        //List<Long> tickets=map.get(id);
        List<TicketDTO> availableTickets=map.get(id).stream()
                                                .map(ticketID->new TicketDTO(ticketID,null,id,Status.FREE.getValue(),null,null))
                                                .collect(Collectors.toList());
       return availableTickets;
    }

    // NU MERGE
    //Decomentezi pe propria raspundere
   /* @Async
    private void saveTicket(Long playId,Long ticketId,Long userId){
        updateTicket(ticketId,new TicketDTO(ticketId,userId,playId,Status.BOOKED.getValue(), LocalDateTime.now().toString(),null));
    }

    public synchronized ResponseEntity<BookResponse> bookTicketAsync(Long playId,Long userId){

        BookResponse bookResponse=new BookResponse();

        Map<Long,List<Long>> map=hazelcastInstance.getMap("availableTickets");
        List<Long> availableTickets = new ArrayList<>(map.get(playId));

        if(availableTickets.size() > 0){
            saveTicket(availableTickets.get(0),playId,userId);
            availableTickets.remove(0);
            map.put(playId,availableTickets);
            bookResponse.setAllowedToBook(true);
            return ResponseEntity.ok(bookResponse);
        }
        else {
            bookResponse.setAllowedToBook(false);
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<BookResponse> tryBookTicketByPlayId(Long playId, String headers){

        String[] headerSplitted=headers.substring("Bearer".length()).trim().split("\\.");
        byte[] userDecoded= Base64.getDecoder().decode(headerSplitted[1]);
        String userCredentialDecoded=new String(userDecoded);
        String userEmail=userCredentialDecoded.split(",")[0].split(":")[1];
        userEmail=userEmail.substring(1,userEmail.length()-1);

        Optional<User> user=userRepository.findByEmail(userEmail);
        if(!user.isPresent()){
            return ResponseEntity.badRequest().build();
        }
        Optional<Ticket> ticket=ticketRepository.findAllByUserId(user.get().getId()).stream()
                .max((t1,t2)->t1.getBookDate().compareTo(t2.getBookDate()));

        if(ticket.isPresent()){
            if(ticket.get().getBookDate().until(LocalDateTime.now(),ChronoUnit.DAYS)>30){
                return bookTicketAsync(playId,user.get().getId());
            }
            else return ResponseEntity.badRequest().build();
        }
        else return bookTicketAsync(playId,user.get().getId());
    }*/
    public Long countAllBookedTicketsByPlayId(Long playId){

        return ticketRepository.countAllBookedTicketsByPlayId(playId);
    }
    //Nu are validare cu 30 de zile ridicata
    //Userul momentan face rezervari cand/cat vrea
    //@Override
    public BookResponse bookTicket(Long playId, String userCredential){
        BookResponse bookResponse=new BookResponse();

        //read All available Tickets from Hazel map
        //Pick the ticket
        //Update hazel Map
        //check if available date is
        List<Ticket> availableTickets=ticketRepository.findAllAvailableByPlayId(playId);
        if(availableTickets==null || availableTickets.size()==0){
            Optional<Ticket> ticket= ticketRepository.findAllByPlayId(playId).stream()
                    .min((t1, t2) -> -t1.getBookDate().compareTo(t2.getBookDate()));
            bookResponse.setExpiredTime(ticket.get().getBookDate().until(LocalDateTime.now(), ChronoUnit.MILLIS));
        }
        else{
                String[] headerSplitted=userCredential.substring("Bearer".length()).trim().split("\\.");
                byte[] userDecoded= Base64.getDecoder().decode(headerSplitted[1]);
                String userCredentialDecoded=new String(userDecoded);
                String userEmail=userCredentialDecoded.split(",")[0].split(":")[1];
                userEmail=userEmail.substring(1,userEmail.length()-1);
                Optional<Ticket> freeTicket=availableTickets.stream().findFirst();
                System.out.println(freeTicket.get()+": "+userCredential);
                PlayDTO play =playMapper.convertPlayToPlayDTO(playRepository.getOne(playId));

                bookResponse.setExpiredTime(null);
                bookResponse.setAllowedToBook(true);

                freeTicket.get().setStatus(Status.BOOKED);
                freeTicket.get().setUserId(userRepository.getUserIdByEmail(userEmail));
                freeTicket.get().setBookDate(LocalDateTime.now());

                updateTicket(freeTicket.get().getId(),ticketMapper.ticketToTicketDTO(freeTicket.get()));
        }
        return bookResponse;
    }
}
