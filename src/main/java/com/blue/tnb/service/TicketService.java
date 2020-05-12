package com.blue.tnb.service;

import com.blue.tnb.dto.BookResponse;
import com.blue.tnb.dto.TicketDTO;
import com.blue.tnb.exception.TicketNotFoundException;
import com.blue.tnb.exception.TicketWithoutUserException;
import com.blue.tnb.model.Play;
import com.blue.tnb.model.Ticket;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;


public interface TicketService {


    List<TicketDTO> getAllTickets();

    TicketDTO getTicketByUserId(Long userId) throws TicketWithoutUserException;

    TicketDTO getTicketById(Long ticketId) throws TicketNotFoundException;

    List<TicketDTO> getAllByPlayId(Long playId) throws TicketNotFoundException;

    //List<TicketDTO> getAllByPlay(Play play) throws TicketNotFoundException;

    Ticket addTicket(TicketDTO ticketDTO) throws ParseException;

    Ticket updateTicket(Long id,TicketDTO ticketDTO);
    Ticket deleteTicket(Long ticketId);

    Long countAvailableTicketsByPlayId(Long playId);

    List<TicketDTO> findAllTicketsByUserId(Long id);
    //List<TicketDTO> getAllByUser(User user);

    List<TicketDTO> findAllAvailableTicketsByPlayId(Long id);

    BookResponse bookTicket(Long playId,Long userId);
}
