package com.blue.tnb.service;

import com.blue.tnb.dto.TicketDTO;
import com.blue.tnb.exception.TicketNotFoundException;
import com.blue.tnb.exception.TicketWithoutUserException;

import java.util.List;


public interface TicketService {

    List<TicketDTO> getAllTickets();

    TicketDTO getTicketByUserId(Long userId) throws TicketWithoutUserException;

    TicketDTO getTicketById(Long ticketId) throws TicketNotFoundException;

    List<TicketDTO> getAllByPlayId(Long playId);
}
