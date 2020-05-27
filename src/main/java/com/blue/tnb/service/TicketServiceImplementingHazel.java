package com.blue.tnb.service;

import com.blue.tnb.constants.Status;
import com.blue.tnb.dto.BookResponse;
import com.blue.tnb.dto.TicketDTO;
import com.blue.tnb.mapper.TicketMapper;
import com.blue.tnb.model.Pair;
import com.blue.tnb.model.Ticket;
import com.blue.tnb.model.User;
import com.blue.tnb.repository.TicketRepository;
import com.blue.tnb.repository.UserRepository;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ILock;
import com.hazelcast.core.IMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;
@Service
public class TicketServiceImplementingHazel {

    @Autowired
    private HazelcastInstance hazelcastInstance;

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    public Ticket addTicket(TicketDTO ticketDTO) throws ParseException {
        IMap<Long,List<Long>> map=hazelcastInstance.getMap("availableTickets");

        Ticket ticket=ticketMapper.ticketDTOToTicket(ticketDTO);
        ticket=ticketRepository.saveAndFlush(ticket);

        ILock lock=hazelcastInstance.getLock("check-available-tickets");
        lock.lock();
        try{
            List<Long> ticketsToBeUpdated= map.get(ticketDTO.getPlayId());
            ticketsToBeUpdated.add(ticket.getId());
            map.put(ticket.getPlayId(),ticketsToBeUpdated);

            return ticket;
        }
        catch (Exception ex){
            System.out.println("Failed to add ticket!Something went wrong:"+ex.getStackTrace().toString().substring(0,100));
            return null;
        }
       finally {
            lock.unlock();
        }
    }

    @Async
    private boolean saveTicket(Long playId,Long ticketId,Long userId) throws ParseException {
        Ticket ticket=updateTicket(ticketId,new TicketDTO(ticketId,userId,playId,Status.BOOKED.getValue(), LocalDateTime.now().toString().replace("T"," "),null));
        return ticket!=null;
    }
    public Ticket updateTicket(Long id, TicketDTO ticketDTO) throws ParseException {
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
        ticketRepository.saveAndFlush(ticket);
        return ticket;
    }

    @Async
    public Ticket deleteTicketById(Ticket ticket){
        ticketRepository.deleteTicket(ticket.getId());
        return ticket;
    }

    public Ticket deleteTicket(Long ticketId) {
        IMap<Long,List<Long>> map= hazelcastInstance.getMap("availableTickets");
        for(Long key:map.keySet()){
            if(map.get(key).indexOf(ticketId)!=-1){
                map.get(key).remove(ticketId);
            }
        }
        Ticket ticket = ticketRepository.getOne(ticketId);
        if(ticket != null){
            ticket=deleteTicketById(ticket);
            return ticket;
        }
        return null;
    }


    public Long countAvailableTicketsByPlayId(Long playId) {
        IMap<Long, List<Long>> ticketsMap=hazelcastInstance.getMap("availableTickets");
        int counter=ticketsMap.get(playId).size();
        return (long) counter;
    }

    public List<TicketDTO> findAllTicketsByCurrentUser(String header){
        String[] headerSplitted=header.substring("Bearer".length()).trim().split("\\.");
        byte[] userDecoded= Base64.getDecoder().decode(headerSplitted[1]);
        String userCredentialDecoded=new String(userDecoded);
        String userEmail=userCredentialDecoded.split(",")[0].split(":")[1];
        userEmail=userEmail.substring(1,userEmail.length()-1);

        Optional<User> user=userRepository.findByEmail(userEmail);
        if(user.isPresent()){
            return findAllTicketsByUserId(user.get().getId());
        }
        else return new ArrayList<TicketDTO>();
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

    public void updateTicketStatusToFree(){
        IMap<Long,List<Long>> map = hazelcastInstance.getMap("availableTickets");
        map.keySet().forEach(playId->map.get(playId).clear());
    }
    @Async
    public void updateBookColumnForUser(Long userId){
        userRepository.updateLastBookForUser(userId);
    }
    public ResponseEntity<BookResponse> bookTicketAsync(Long playId,Long userId){
        BookResponse bookResponse=new BookResponse();

        ILock reserveTicketLock=hazelcastInstance.getLock("check-available-tickets");
        reserveTicketLock.lock();
        try{
            IMap<Long,List<Long>> ticketMap=hazelcastInstance.getMap("availableTickets");
            List<Long> availableTickets = new ArrayList<>(ticketMap.get(playId));

            if(availableTickets.size() > 0){

                saveTicket(playId,availableTickets.get(0),userId);
                updateBookColumnForUser(userId);
                IMap<Long,LocalDateTime> userMap = hazelcastInstance.getMap("userTickets");

                LocalDateTime userDetails=userMap.get(userId);
                userDetails=LocalDateTime.now();
                userMap.put(userId,userDetails);

                availableTickets.remove(0);
                ticketMap.put(playId,availableTickets);

                bookResponse.setAllowedToBook(true);
                return ResponseEntity.ok(bookResponse);
            }
            else {
                bookResponse.setAllowedToBook(false);
                return ResponseEntity.badRequest().build();
            }
        }
        catch (Exception ex){
            System.out.println("Failed to reserve tickets.Exception "+ Arrays.toString(ex.getStackTrace()));
            return ResponseEntity.notFound().build();
        }
        finally {
            reserveTicketLock.unlock();
        }
    }
    public ResponseEntity<BookResponse> bookTicket(Long playId, Long userId) {
        if(countAvailableTicketsByPlayId(playId)>0){

            Optional<User> user=userRepository.findById(userId);
            if(!user.isPresent()){
                return ResponseEntity.badRequest().build();
            }
            IMap<Long,LocalDateTime> userLastBookDate=hazelcastInstance.getMap("userTickets");
            if(userLastBookDate.get(user.get().getId())!=null){
                if(userLastBookDate.get(user.get().getId()).until(LocalDateTime.now(),ChronoUnit.SECONDS)>=30*24*3600){
                    return bookTicketAsync(playId,user.get().getId());
                }
                else return ResponseEntity.badRequest().build();
            }
            else return bookTicketAsync(playId,user.get().getId());
        }
        return  ResponseEntity.badRequest().build();
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
            IMap<Long,Pair<List<Pair<Long,LocalDateTime>>,LocalDateTime>> userTickets=hazelcastInstance.getMap("userTickets");
            if(userTickets.get(user.get().getId()).snd!=null){
                if(userTickets.get(user.get().getId()).snd.until(LocalDateTime.now(),ChronoUnit.SECONDS)>=30*24*3600){
                    return bookTicketAsync(playId,user.get().getId());
                }
                else return ResponseEntity.badRequest().build();
            }
            else return bookTicketAsync(playId,user.get().getId());
        }
        return  ResponseEntity.badRequest().build();
    }
//    public void clearUserLastBookedDate(Long userId, Long playId){
//        userRepository.clearUserLastBookedDate(userId,playId);
//    }
//    public synchronized Boolean unbookTicketforLoggedUser(String userCredential, Long playId){
//        String[] headerSplitted=userCredential.substring("Bearer".length()).trim().split("\\.");
//        byte[] userDecoded= Base64.getDecoder().decode(headerSplitted[1]);
//        String userCredentialDecoded=new String(userDecoded);
//        String userEmail=userCredentialDecoded.split(",")[0].split(":")[1];
//        userEmail=userEmail.substring(1,userEmail.length()-1);
//
//        Long userId=userRepository.getUserIdByEmail(userEmail);
//
//
//    }
}
