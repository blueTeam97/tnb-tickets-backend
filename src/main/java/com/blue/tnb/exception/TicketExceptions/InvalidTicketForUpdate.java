package com.blue.tnb.exception.TicketExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class InvalidTicketForUpdate extends Exception{
    public InvalidTicketForUpdate(){
        super("Invalid ticket for update!Check ticket status, date or id");
    }
}
