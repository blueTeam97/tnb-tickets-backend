package com.blue.tnb.service;

import com.blue.tnb.dto.TicketDTO;
import com.blue.tnb.exception.TicketNotFoundException;
import com.blue.tnb.exception.TicketWithoutUserException;
import com.blue.tnb.mapper.TicketMapper;
import com.blue.tnb.model.Play;
import com.blue.tnb.model.Ticket;
import com.blue.tnb.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {

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

 /*   @Override
    public List<TicketDTO> getAllByPlay(Play play) {
        return ticketMapper.convertTicketToTicketDTOList(ticketRepository.findAllByPlayId(play.getId()));
    }*/

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
        Optional<Ticket> existingTicket=ticketRepository.findById(ticketId);
        if(existingTicket.isPresent()){
            ticketRepository.delete(existingTicket.get());
            return existingTicket.get();
        }
        else return null;
    }
}
