package com.blue.tnb.exception.TicketExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TicketsNumberException extends Exception {
    public TicketsNumberException() {
        super("Wrong Tickets Number");
    }
}
