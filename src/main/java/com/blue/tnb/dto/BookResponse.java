package com.blue.tnb.dto;

import java.util.Date;
import java.util.Objects;

public class BookResponse {

    private Long expiredTime;

    private boolean allowedToBook;

    public BookResponse(){}

    public BookResponse(TicketDTO ticketDTO, PlayDTO playDTO, Long expiredTime) {
        this.expiredTime = expiredTime;
    }

    public boolean isAllowedToBook() {
        return allowedToBook;
    }

    public void setAllowedToBook(boolean allowedToBook) {
        this.allowedToBook = allowedToBook;
    }

    public Long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Long expiredTime) {
        this.expiredTime = expiredTime;
    }
}
