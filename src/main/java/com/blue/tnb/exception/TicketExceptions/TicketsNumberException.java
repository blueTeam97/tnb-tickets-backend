package com.blue.tnb.exception.TicketExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TicketsNumberException extends Exception {
    public TicketsNumberException() {
        super("Wrong Tickets Number");
    }
}
