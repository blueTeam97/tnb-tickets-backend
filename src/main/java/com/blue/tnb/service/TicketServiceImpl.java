package com.blue.tnb.service;

import com.blue.tnb.constants.DateUtils;
import com.blue.tnb.constants.Status;
import com.blue.tnb.dto.BookResponse;
import com.blue.tnb.dto.PlayDTO;
import com.blue.tnb.dto.TicketDTO;
import com.blue.tnb.exception.TicketExceptions.TicketNotFoundException;
import com.blue.tnb.exception.TicketExceptions.TicketWithoutUserException;
import com.blue.tnb.mapper.PlayMapper;
import com.blue.tnb.mapper.TicketMapper;
import com.blue.tnb.model.Ticket;
import com.blue.tnb.model.User;
import com.blue.tnb.repository.PlayRepository;
import com.blue.tnb.repository.TicketRepository;
import com.blue.tnb.repository.UserRepository;
import com.blue.tnb.validator.PlayValidator;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.swing.text.html.Option;
import java.text.ParseException;;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.locks.Lock;
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

    @Autowired
    private PlayValidator playValidator;

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
        Status status=ticketDTO.equals("free")?Status.FREE:(ticketDTO.getStatus().equals("booked")?Status.BOOKED:Status.PICKEDUP);
        ticket.setStatus(status);
        ticket.setUserId(ticketDTO.getUserId());
        String date;

        if(StringUtils.isEmpty(ticketDTO.getBookDate())){
            ticket.setBookDate(null);
        }
        else {
            date=ticketDTO.getBookDate();
            date=date.indexOf('.')>=0?date.substring(0,date.indexOf('.')):date;
            ticket.setBookDate(DateUtils.convertStringToLocalDateTime(date));
        }

        if(StringUtils.isEmpty(ticketDTO.getPickUpDate())){
            ticket.setPickUpDate(null);
        }
        else {
            date =ticketDTO.getPickUpDate();
            date=date.indexOf('.')>=0?date.substring(0,date.indexOf('.')):date;
            ticket.setPickUpDate(DateUtils.convertStringToLocalDateTime(date));
        }

        ticket.getPlay().setTicketList(null);
        return ticketRepository.saveAndFlush(ticket);
    }

    //@Override
    public Ticket deleteTicket(Long ticketId) {
        Optional<Ticket> existingTicket = ticketRepository.findById(ticketId);
        if (existingTicket.isPresent()) {
            ticketRepository.deleteTicket(ticketId);
            return existingTicket.get();
        } else return null;
    }

   // @Override
    public Long countAvailableTicketsByPlayId(Long playId) {
        return ticketRepository.countAllAvailableByPlayId(playId);
    }
    public List<TicketDTO> findAllTicketsByCurrentUser(String header){
        String[] headerSplitted=header.substring("Bearer".length()).trim().split("\\.");
        byte[] userDecoded= Base64.getDecoder().decode(headerSplitted[1]);
        String userCredentialDecoded=new String(userDecoded);
        String userEmail=userCredentialDecoded.split(",")[0].split(":")[1];
        userEmail=userEmail.substring(1,userEmail.length()-1);

        Optional<User> user=userRepository.findByEmail(userEmail);
        return user.isPresent()?findAllTicketsByUserId(user.get().getId()):null;
    }
  //  @Override
    public List<TicketDTO> findAllTicketsByUserId(Long id) {
        return ticketMapper.convertTicketToTicketDTOList(ticketRepository.findAllByUserId(id));
    }

   // @Override
    public List<TicketDTO> findAllAvailableTicketsByPlayIdUsingHazel(Long id) {
        Map<Long,List<Long>> map=hazelcastInstance.getMap("availableTickets");
        List<TicketDTO> availableTickets=map.get(id).stream()
                                                .map(ticketID->new TicketDTO(ticketID,null,id,Status.FREE.getValue(),null,null))
                                                .collect(Collectors.toList());
       return availableTickets;
    }
    public List<TicketDTO> findAllAvailableTicketsByPlayId(Long id) {
        List<TicketDTO> availableTickets=ticketRepository.findAllAvailableByPlayId(id).stream()
                .map(ticketMapper::ticketToTicketDTO)
                .collect(Collectors.toList());
        return availableTickets;
    }

    private boolean updateTicketForPickUp(Long playId,Long ticketId,Long userId){
        String date=LocalDateTime.now().toString().replace("T"," ");
        date=date.substring(0,date.indexOf('.'));
        return updateTicket(ticketId,new TicketDTO(ticketId, userId, playId, Status.PICKEDUP.getValue(), null, date))!=null;
    }
   /* private boolean updateTicketForPickUpV2(Long playId,Ticket ticket,Long userId){
        String date=LocalDateTime.now().toString().replace("T"," ");
        date=date.substring(0,date.indexOf('.'));
        return updateTicket(ticket.getId(),new TicketDTO(ticket.getId(), userId, playId, Status.PICKEDUP.getValue(), ticket.getBookDate().toString().replace("T"," "), date))!=null;
    }*/

    public boolean pickUpTicketByUserAndTicketId(Long ticketId,Long userId){
        Ticket ticket;
        ticket=ticketRepository.findOneById(ticketId);
        if(ticket!=null){
            Long playId=ticket.getPlayId();
            boolean updateIsOk = updateTicketForPickUp(playId,ticket.getId(),userId);
            return true;
        }
        return false;
    }

    // NU MERGE
    //Decomentezi pe propria raspundere
   @Async
   void saveTicket(Long playId, Long ticketId, Long userId){
        updateTicket(ticketId,new TicketDTO(ticketId,userId,playId,Status.BOOKED.getValue(), LocalDateTime.now().toString().replace("T"," "),null));
    }
    public ResponseEntity<BookResponse> bookTicketAsync(Long playId,Long userId){

        BookResponse bookResponse=new BookResponse();

        Lock reserveTicketLock=hazelcastInstance.getLock("check-available-tickets");
        reserveTicketLock.lock();
        try{
            Map<Long,List<Long>> map=hazelcastInstance.getMap("availableTickets");
            List<Long> availableTickets = new ArrayList<>(map.get(playId));

            if(availableTickets.size() > 0){

                saveTicket(playId,availableTickets.get(0),userId);

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
        catch (Exception ex){
            System.out.println("Failed to reserve tickets.Exception "+ Arrays.toString(ex.getStackTrace()));
            return ResponseEntity.notFound().build();
        }
        finally {
            reserveTicketLock.unlock();
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
    }
    public ResponseEntity<BookResponse> tryBookTicketByPlayIdTest(Long playId, Long userId){

        Optional<Ticket> ticket=ticketRepository.findAllByUserId(userId).stream()
                .max((t1,t2)->t1.getBookDate().compareTo(t2.getBookDate()));

        return bookTicketAsync(playId,userId);
    }

    public Long countAllBookedTicketsByPlayId(Long playId){

        return ticketRepository.countAllBookedTicketsByPlayId(playId);
    }
    //@Override
    public synchronized BookResponse bookTicket(Long playId, String userCredential){
        BookResponse bookResponse=new BookResponse();

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

                Optional<User> user=userRepository.findByEmail(userEmail);

                if(user.isPresent()){
                    if(user.get().getLastBook()!=null &&
                            user.get().getLastBook().until(LocalDateTime.now(),ChronoUnit.DAYS)>=30){
                        bookResponse.setAllowedToBook(true);
                        Ticket newTicket=availableTickets.get(0);
                        try{
                            userRepository.updateLastBookForUser(user.get().getId());
                        }
                        catch(Exception ex){
                            return null;
                        }
                        saveTicket(playId,newTicket.getId(),user.get().getId());
                    }
                    else if(user.get().getLastBook()==null){
                        bookResponse.setAllowedToBook(true);
                        Ticket newTicket=availableTickets.get(0);
                        try{
                            userRepository.updateLastBookForUser(user.get().getId());
                        }
                        catch(Exception ex){
                            return null;
                        }
                        saveTicket(playId,newTicket.getId(),user.get().getId());
                    }
                    else bookResponse.setAllowedToBook(false);
                }
        }
        return bookResponse;
    }
    public Boolean getStatusForLoggedUser(String userCredential){
        String[] headerSplitted=userCredential.substring("Bearer".length()).trim().split("\\.");
        byte[] userDecoded= Base64.getDecoder().decode(headerSplitted[1]);
        String userCredentialDecoded=new String(userDecoded);
        String userEmail=userCredentialDecoded.split(",")[0].split(":")[1];
        userEmail=userEmail.substring(1,userEmail.length()-1);
        return ticketRepository.getSubscribeStateForUser(userEmail);
    }
    public List<String> findEmailsForTicketStatusBooked(){
        return ticketRepository.findEmailsForTicketStatusBooked();
    }

    public void updateTicketStatusToFree(){
        ticketRepository.updateTicketStatusToFree();
    }

    public List<TicketDTO> findAllBookedTicketsByPlayId(Long id) {
        List<TicketDTO> bookedTickets = ticketRepository.findAllBookedTicketsByPlayId(id).stream()
                .map(ticketMapper::ticketToTicketDTO)
                .collect(Collectors.toList());
        return bookedTickets;
    }

    public void freeBookedTicketsOncePlayDateInThePast(){
        ticketRepository.freeBookedTicketsOncePlayDateInThePast();
    }

    public Optional<String> findEmailByUserId(Long id) {
        return userRepository.getEmailByUserId(id);
    }

    public Boolean unbookTicketforLoggedUser(String userCredential,Long playId){
        String[] headerSplitted=userCredential.substring("Bearer".length()).trim().split("\\.");
        byte[] userDecoded= Base64.getDecoder().decode(headerSplitted[1]);
        String userCredentialDecoded=new String(userDecoded);
        String userEmail=userCredentialDecoded.split(",")[0].split(":")[1];
        userEmail=userEmail.substring(1,userEmail.length()-1);

        Long userId=userRepository.getUserIdByEmail(userEmail);

        try{
            ticketRepository.unbookTicket(userId,playId);
            return true;
        }
        catch(Exception ex){
            return false;
        }
    }
}
