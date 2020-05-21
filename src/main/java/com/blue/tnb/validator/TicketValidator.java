package com.blue.tnb.validator;

import com.blue.tnb.dto.TicketDTO;
import com.blue.tnb.model.Ticket;
import com.blue.tnb.repository.PlayRepository;
import com.blue.tnb.repository.TicketRepository;
import org.apache.commons.validator.GenericValidator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.swing.text.html.Option;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

@Component
public class TicketValidator {

    private String correctDate;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private PlayRepository playRepository;

    public boolean validateTicketIdForUpdate(Long ticketId){
        return ticketRepository.findOneById(ticketId)!=null;
    }
    public boolean validateTicketDate(String ticketDateTimeAsString,boolean isForUpdate){
        if(StringUtils.isEmpty(ticketDateTimeAsString) || ticketDateTimeAsString.equals(" "))
            return !isForUpdate;
        else{
            if(GenericValidator.isDate(ticketDateTimeAsString, "yyyy-MM-dd HH:mm:ss", true)){
                return true;
            }
            else return GenericValidator.isDate(ticketDateTimeAsString, "yyyy-MM-dd HH:mm", true);
        }
    }
    public boolean validateTicketId(Long ticketId){

        if(ticketId==null){
            return false;
        }
        else{
           try{
               Ticket ticket= ticketRepository.findOneById(ticketId);
               return ticket==null;
           }
           catch(Exception ex){
               return true;
           }
        }
    }

    public boolean validateTicketPlayId(Long ticketPlayId){

       if(ticketPlayId==null){
           return false;
       }
       else{
           return playRepository.findById(ticketPlayId).isPresent();
       }
    }
    public boolean validateTicketStatus(String ticketDTOStatus){
        return ticketDTOStatus.equals("free") || ticketDTOStatus.equals("booked") || ticketDTOStatus.equals("pickedup");
    }
    public boolean validateTicketForUpdate(@NotNull TicketDTO ticketDTO){
        if(ticketDTO.getId()!=null){
            return validateTicketIdForUpdate(ticketDTO.getId());
        }
        else{
            boolean ok=validateTicketPlayId(ticketDTO.getPlayId());
            System.out.println(ok);
            if(validateTicketDate(ticketDTO.getBookDate(),true)){
                ticketDTO.getBookDate().replace("T"," ");
            }
            if(validateTicketDate(ticketDTO.getPickUpDate(),true)){
                ticketDTO.getPickUpDate().replace("T"," ");
            }
            return validateTicketPlayId(ticketDTO.getPlayId()) && validateTicketStatus(ticketDTO.getStatus());
        }
    }
    public boolean validateTicket(@org.jetbrains.annotations.NotNull TicketDTO ticketDTO){
       if(ticketDTO.getId()!=null){
           return validateTicketId(ticketDTO.getId());
       }
       else{
           boolean ok=validateTicketPlayId(ticketDTO.getPlayId());
           return validateTicketPlayId(ticketDTO.getPlayId()) &&
                   validateTicketStatus(ticketDTO.getStatus()) &&
                   validateTicketDate(ticketDTO.getBookDate(),false) &&
                   validateTicketDate(ticketDTO.getPickUpDate(), false);
       }
    }
}
