package com.blue.tnb.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class InvalidDateException extends Exception {

    public InvalidDateException(){
        super("Invalid Date.Required format: 'yyyy-MM-dd HH:mm:ss'");
    }
}
