package com.blue.tnb.dto;

import java.util.Date;
import java.util.Objects;

public class BookResponse {

    private TicketDTO ticketDTO; //Scapa de el

    private PlayDTO playDTO;

    private Date expiredTime;


    public BookResponse(){}

    public BookResponse(TicketDTO ticketDTO, PlayDTO playDTO, Date expiredTime) {
        this.ticketDTO = ticketDTO;
        this.playDTO = playDTO;
        this.expiredTime = expiredTime;
    }

    @Override
    public String toString() {
        return "BookResponse{" +
                "ticketDTO=" + ticketDTO +
                ", playDTO=" + playDTO +
                ", expiredTime=" + expiredTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookResponse)) return false;
        BookResponse that = (BookResponse) o;
        return Objects.equals(ticketDTO, that.ticketDTO) &&
                Objects.equals(playDTO, that.playDTO) &&
                Objects.equals(expiredTime, that.expiredTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticketDTO, playDTO, expiredTime);
    }

    public TicketDTO getTicketDTO() {
        return ticketDTO;
    }

    public void setTicketDTO(TicketDTO ticketDTO) {
        this.ticketDTO = ticketDTO;
    }


    public PlayDTO getPlayDTO() {
        return playDTO;
    }

    public void setPlayDTO(PlayDTO playDTO) {
        this.playDTO = playDTO;
    }

    public Date getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Date expiredTime) {
        this.expiredTime = expiredTime;
    }
}
