package com.blue.tnb.exception.TicketExceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TicketWithoutUserException extends Exception {
    public TicketWithoutUserException(Long userId){
        super("Couldn't find any ticket for userID="+userId);
    }
}
