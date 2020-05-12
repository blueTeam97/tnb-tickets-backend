package com.blue.tnb.exception.PlayExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PlayNotFoundException extends Exception{
    public PlayNotFoundException() {
        super("Play Not Found Exception");
    }
}
