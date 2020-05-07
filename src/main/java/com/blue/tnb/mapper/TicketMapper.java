package com.blue.tnb.mapper;

import com.blue.tnb.dto.TicketDTO;
import com.blue.tnb.model.Ticket;

import java.util.List;


public interface TicketMapper {

    List<Ticket> convertTicketDTOToTicketList(List<TicketDTO> tickets);

    List<TicketDTO> convertTicketToTicketDTOList(List<Ticket> tickets);

    Ticket ticketDTOToTicket(TicketDTO ticketDTO);
    TicketDTO ticketToTicketDTO(Ticket ticket);
}
