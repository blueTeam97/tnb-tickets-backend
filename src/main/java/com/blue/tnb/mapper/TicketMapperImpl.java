package com.blue.tnb.mapper;

import com.blue.tnb.dto.TicketDTO;
import com.blue.tnb.model.Ticket;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Component
public class TicketMapperImpl implements TicketMapper {
    @Override
    public List<Ticket> convertTicketDTOToTicketList(List<TicketDTO> tickets) throws ParseException {
        if(tickets==null){
            return null;
        }
        else {
            if(tickets.size()==0){
                return new ArrayList<>();
            }
            else{
                List<Ticket> ticketList=new ArrayList<>();
                for(TicketDTO ticketDTO:tickets){
                    ticketList.add(new Ticket(ticketDTO));
                }
                return ticketList;
            }
        }
    }

    @Override
    public List<TicketDTO> convertTicketToTicketDTOList(List<Ticket> tickets) {
        if(tickets==null){
            return null;
        }
        else {
            if(tickets.size()==0){
                return new ArrayList<TicketDTO>();
            }
            else{
                List<TicketDTO> ticketList=new ArrayList<>();
                for(Ticket ticket:tickets){
                    ticketList.add(new TicketDTO(ticket));
                }
                return ticketList;
            }
        }
    }

    @Override
    public Ticket ticketDTOToTicket(TicketDTO ticketDTO) throws ParseException {
        return ticketDTO==null?null:new Ticket(ticketDTO);
    }

    @Override
    public TicketDTO ticketToTicketDTO(Ticket ticket) {
        return ticket==null?null:new TicketDTO(ticket);
    }
}
