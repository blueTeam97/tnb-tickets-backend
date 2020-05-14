package com.blue.tnb.mapper;

import com.blue.tnb.constants.Status;
import com.blue.tnb.dto.TicketDTO;
import com.blue.tnb.model.Ticket;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class TicketMapperImpl implements TicketMapper {
    @Override
    public List<Ticket> convertTicketDTOToTicketList(List<TicketDTO> tickets) {
        if(tickets == null){
            return null;
        }
        else {
            if(tickets.size() == 0){
                return new ArrayList<>();
            }
            else{
                List<Ticket> ticketList = new ArrayList<>();
                for(TicketDTO ticketDTO: tickets){
                    ticketList.add(ticketDTOToTicket(ticketDTO));
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
    public Ticket ticketDTOToTicket(TicketDTO ticketDTO)  {
        if(ticketDTO == null) {
            return null;
        }
        else {
            Ticket ticket = new Ticket();
            ticket.setId(ticketDTO.getId());
            ticket.setUserId(ticketDTO.getUserId());
            ticket.setPlayId(ticketDTO.getPlayId());

            switch (ticketDTO.getStatus().toLowerCase()) {
                case "free":
                    ticket.setStatus(Status.FREE);
                    break;
                case "booked":
                    ticket.setStatus(Status.BOOKED);
                    break;
                case "pickedup":
                    ticket.setStatus(Status.PICKEDUP);
                    break;
            }

            if (!StringUtils.isEmpty(ticketDTO.getBookDate())) {
                ticket.setBookDate(convertStringToLocalDateTime(ticketDTO.getBookDate()));
            } else {
                ticket.setBookDate(null);
            }

            if (!StringUtils.isEmpty(ticketDTO.getPickUpDate())) {
                ticket.setPickUpDate(convertStringToLocalDateTime(ticketDTO.getPickUpDate()));
            }
            else {
                ticket.setPickUpDate(null);
            }
            return ticket;
        }
    }

    @Override
    public TicketDTO ticketToTicketDTO(Ticket ticket) {
        return ticket==null?null:new TicketDTO(ticket);
    }


    public LocalDateTime convertStringToLocalDateTime(String dateAsString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dateAsString, formatter);
        return dateTime;
    }


}

