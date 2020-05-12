package com.blue.tnb.exception.PlayExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidDateException extends Exception{
    public InvalidDateException() {
        super("Invalid Date Exception");
    }
}
