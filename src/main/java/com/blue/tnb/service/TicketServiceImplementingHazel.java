package com.blue.tnb.service;

import com.blue.tnb.constants.Status;
import com.blue.tnb.dto.BookResponse;
import com.blue.tnb.dto.TicketDTO;
import com.blue.tnb.exception.TicketExceptions.TicketNotFoundException;
import com.blue.tnb.exception.TicketExceptions.TicketWithoutUserException;
import com.blue.tnb.mapper.TicketMapper;
import com.blue.tnb.model.Pair;
import com.blue.tnb.model.Ticket;
import com.blue.tnb.model.User;
import com.blue.tnb.repository.TicketRepository;
import com.blue.tnb.repository.UserRepository;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import jdk.vm.ci.meta.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class TicketServiceImplementingHazel {

    @Autowired
    private HazelcastInstance hazelcastInstance;

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

   /* public List<TicketDTO> getAllTickets() {
        return null;
    }*/


  /*  public TicketDTO getTicketByUserId(Long userId) throws TicketWithoutUserException {
        return null;
    }*/


    public TicketDTO getTicketById(Long ticketId) throws TicketNotFoundException {
        return null;
    }


    public List<TicketDTO> getAllByPlayId(Long playId) throws TicketNotFoundException {
        return null;
    }


    public Ticket addTicket(TicketDTO ticketDTO) throws ParseException {
        return null;
    }

    @Async
    private boolean saveTicket(Long playId,Long ticketId,Long userId) {
        Ticket ticket=updateTicket(ticketId,new TicketDTO(ticketId,userId,playId,Status.BOOKED.getValue(), LocalDateTime.now().toString().replace("T"," "),null));
        return ticket!=null;
    }
    public Ticket updateTicket(Long id, TicketDTO ticketDTO) {
        Ticket ticket=ticketRepository.getOne(id);
        Status status=ticketDTO.equals("free")? Status.FREE:(ticketDTO.getStatus().equals("booked")?Status.BOOKED:Status.PICKEDUP);
        ticket.setStatus(status);
        ticket.setUserId(ticketDTO.getUserId());
        String date;
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if(StringUtils.isEmpty(ticketDTO.getBookDate())){
            ticket.setBookDate(null);
        }
        else {
            date=ticketDTO.getBookDate();
            ticket.setBookDate(LocalDateTime.parse(date.indexOf('.')>=0?date.substring(0,date.indexOf('.')):date,formatter));
        }

        if(StringUtils.isEmpty(ticketDTO.getPickUpDate())){
            ticket.setPickUpDate(null);
        }
        else {
            date =ticketDTO.getPickUpDate();
            ticket.setPickUpDate(LocalDateTime.parse(date.indexOf('.')>=0?date.substring(0,date.indexOf('.')):date,formatter));
        }

        ticket.getPlay().setTicketList(null);
        return ticketRepository.saveAndFlush(ticket);
    }


   /* public Ticket deleteTicket(Long ticketId) {
        return null;
    }*/


    public Long countAvailableTicketsByPlayId(Long playId) {
        IMap<Long, List<Long>> ticketsMap=hazelcastInstance.getMap("availableTickets");
        int counter=ticketsMap.get(playId).size();
        return (long) counter;
    }


    public List<TicketDTO> findAllTicketsByUserId(Long id) {
        IMap<Long,List<Pair<Long, LocalDateTime>>> userTicketsMap=hazelcastInstance.getMap("userTickets");
        return userTicketsMap.get(id).stream()
                .map(ticket->ticketMapper.ticketToTicketDTO(ticketRepository.findOneById(ticket.fst)))
                .collect(Collectors.toList());
    }


    public List<TicketDTO> findAllAvailableTicketsByPlayId(Long id) {
        IMap<Long,List<Long>> ticketsMap=hazelcastInstance.getMap("availableTickets");
        List<TicketDTO> availableTickets=ticketsMap.get(id).stream()
                                                    .map(ticketId->ticketMapper.ticketToTicketDTO(ticketRepository.findOneById(ticketId)))
                                                    .collect(Collectors.toList());
        return availableTickets;
    }

    public ResponseEntity<BookResponse> bookTicketAsync(Long playId,Long userId){

        
    }
    public ResponseEntity<BookResponse> bookTicket(Long playId, String headers) {
        if(countAvailableTicketsByPlayId(playId)>0){
            String[] headerSplitted=headers.substring("Bearer".length()).trim().split("\\.");
            byte[] userDecoded= Base64.getDecoder().decode(headerSplitted[1]);
            String userCredentialDecoded=new String(userDecoded);
            String userEmail=userCredentialDecoded.split(",")[0].split(":")[1];
            userEmail=userEmail.substring(1,userEmail.length()-1);

            Optional<User> user=userRepository.findByEmail(userEmail);
            if(!user.isPresent()){
                return ResponseEntity.badRequest().build();
            }
            IMap<Long,List<Pair<Long,LocalDateTime>>> userTickets=hazelcastInstance.getMap("userTickets");

            Optional<Pair<Long,LocalDateTime>> ticket=userTickets.get(user.get().getId()).stream()
                                                .max((t1,t2)->t1.snd.compareTo(t2.snd));
            if(ticket.isPresent()){
                if(ticket.get().snd.until(LocalDateTime.now(), ChronoUnit.DAYS)>30){
                    return bookTicketAsync(playId,user.get().getId());
                }
                else return ResponseEntity.badRequest().build();
            }
            else return bookTicketAsync(playId,user.get().getId());
        }
        return null;
    }
}
