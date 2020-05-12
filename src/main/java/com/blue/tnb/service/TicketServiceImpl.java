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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private PlayMapper playMapper;
    @Autowired
    private PlayRepository playRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketMapper ticketMapper;

    @Override
    public List<TicketDTO> getAllTickets() {
        return ticketRepository.findAll().stream()
                                .map(ticketMapper::ticketToTicketDTO)
                                .collect(Collectors.toList());
    }

    @Override
    public TicketDTO getTicketByUserId(Long userId) throws TicketWithoutUserException {
        return ticketRepository.findAllByUserId(userId).stream()
                                .filter(ticket-> ticket.getUserId().equals(userId))
                                .map(ticketMapper::ticketToTicketDTO)
                                .findAny().orElseThrow(()->new TicketWithoutUserException(userId));
    }

    @Override
    public TicketDTO getTicketById(Long ticketId) throws TicketNotFoundException {
        return ticketMapper.ticketToTicketDTO(ticketRepository.getOne(ticketId));
    }

    @Override
    public List<TicketDTO> getAllByPlayId(Long id) {
        return ticketMapper.convertTicketToTicketDTOList(ticketRepository.findAllByPlayId(id));
    }

    @Override
    public Ticket addTicket(TicketDTO ticketDTO) throws ParseException {
        return ticketRepository.saveAndFlush(ticketMapper.ticketDTOToTicket(ticketDTO));
    }

    public Ticket updateTicket(Long id, TicketDTO ticketDTO){
        Ticket ticket=ticketRepository.getOne(id);
        return ticketRepository.saveAndFlush(ticket);
    }

    @Override
    public Ticket deleteTicket(Long ticketId) {
        Optional<Ticket> existingTicket = ticketRepository.findById(ticketId);
        if (existingTicket.isPresent()) {
            ticketRepository.delete(existingTicket.get());
            return existingTicket.get();
        } else return null;
    }

    @Override
    public Long countAvailableTicketsByPlayId(Long playId) {
        return ticketRepository.countAllAvailableByPlayId(playId);
    }

    @Override
    public List<TicketDTO> findAllTicketsByUserId(Long id) {
        return ticketMapper.convertTicketToTicketDTOList(ticketRepository.findAllByUserId(id));
    }

    @Override
    public List<TicketDTO> findAllAvailableTicketsByPlayId(Long id) {
        return ticketRepository.findAllAvailableByPlayId(id).stream()
                                                            .map(ticketMapper::ticketToTicketDTO)
                                                            .collect(Collectors.toList());
    }

    @Override
    public BookResponse bookTicket(Long playId, Long userId) {
        BookResponse bookResponse=new BookResponse();

        List<Ticket> availableTickets=ticketRepository.findAllAvailableByPlayId(playId);
        if(availableTickets==null || availableTickets.size()==0){
            Optional<Ticket> ticket= ticketRepository.findAllByPlayId(playId).stream()
                                        .min((t1, t2) -> -t1.getBookDate().compareTo(t2.getBookDate()));
            Date currentTime=new Date(System.currentTimeMillis());
            Date diff=new Date(currentTime.getTime()-ticket.get().getBookDate().getTime());
            bookResponse.setExpiredTime(diff);
        }
        else{
            Optional<Ticket> freeTicket=availableTickets.stream().findAny();
            PlayDTO play =playMapper.convertPlayToPlayDTO(playRepository.getOne(playId));
            play.setTicketDTOList(null);
            bookResponse.setPlayDTO(play);
            bookResponse.setTicketDTO(ticketMapper.ticketToTicketDTO(freeTicket.get()));
            bookResponse.setExpiredTime(null);
            freeTicket.get().setStatus(Status.BOOKED);
            freeTicket.get().setUserId(userId);
            freeTicket.get().setBookDate(new Date(System.currentTimeMillis()));
            bookResponse.setTicketDTO(ticketMapper.ticketToTicketDTO(freeTicket.get()));
            updateTicket(freeTicket.get().getId(),ticketMapper.ticketToTicketDTO(freeTicket.get()));
        }
        return bookResponse;
    }
}
