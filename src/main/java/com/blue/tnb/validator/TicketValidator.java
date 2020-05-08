package com.blue.tnb.validator;

import com.blue.tnb.dto.TicketDTO;
import com.blue.tnb.model.Ticket;
import com.blue.tnb.repository.PlayRepository;
import com.blue.tnb.repository.TicketRepository;
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

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private PlayRepository playRepository;

    public boolean validateTicketIdForUpdate(Long ticketId){
        return ticketRepository.findOneById(ticketId)!=null;
    }
    public boolean validateTicketDate(String ticketDate){

        if(StringUtils.isEmpty(ticketDate))
            return false;
        if(ticketDate.contentEquals("T")){
            return false;
        }
        else {
            String[] dateSplitted=ticketDate.split(" ");
            char dateDelimiter=' ';
            char timeDelimiter=' ';
            for(char ch:dateSplitted[0].toCharArray()){
                if(ch<'0' || ch>'9'){
                    dateDelimiter=ch;
                    break;
                }
            }
            for(char ch:dateSplitted[1].toCharArray()){
                if(ch<'0' || ch>'9'){
                    timeDelimiter=ch;
                    break;
                }
            }
            StringBuilder format=new StringBuilder();
            format.append("yyyy").append(dateDelimiter).append("MM")
                                 .append(dateDelimiter).append("dd");
            format.append(" ");
            format.append("hh").append(timeDelimiter)
                    .append("mm").append(timeDelimiter)
                    .append("ss");
            DateTimeFormatter formatter= DateTimeFormatter.ofPattern(format.toString());
            System.out.println(format.toString());
            try{
                formatter.parse(ticketDate);
            }
            catch (Exception ex){
                return false;
            }
            return true;
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
           return playRepository.findOneById(ticketPlayId).isPresent();
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
            if(validateTicketDate(ticketDTO.getBookDate())){
                ticketDTO.getBookDate().replace("T"," ");
            }
            if(validateTicketDate(ticketDTO.getPickUpDate())){
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
           System.out.println(ok);
           return validateTicketPlayId(ticketDTO.getPlayId()) && validateTicketStatus(ticketDTO.getStatus());
       }
    }
}
