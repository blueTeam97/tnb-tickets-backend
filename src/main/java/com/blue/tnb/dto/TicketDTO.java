package com.blue.tnb.dto;

import com.blue.tnb.constants.Status;
import com.blue.tnb.model.Ticket;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.Objects;

public class TicketDTO {

    @NotEmpty(message = "A ticket MUST have an ID associated")
    private Long id;

    private Long userId;

    @NotEmpty(message = "A ticket MUST have a play associated")
    private Long playId;

    private String status;

    private Date bookDate;

    private Date pickUpDate;

/*    private PlayDTO play;

    private UserDTO user;*/

    public TicketDTO(Ticket ticket) {
        this.playId=ticket.getPlayId();
        this.userId=ticket.getUserId();
        this.status=ticket.getStatus().getValue();
        this.bookDate=ticket.getBookDate();
        this.pickUpDate=ticket.getPickUpDate();
    }

    public TicketDTO(Long userId, Long playId, Date bookDate, Date pickUpDate) {
        this.userId = userId;
        this.playId = playId;
        this.bookDate = bookDate;
        this.pickUpDate = pickUpDate;
        this.status=Status.FREE.getValue();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPlayId() {
        return playId;
    }

    public void setPlayId(Long playId) {
        this.playId = playId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status.getValue();
    }

    public Date getBookDate() {
        return bookDate;
    }

    public void setBookDate(Date bookDate) {
        this.bookDate = bookDate;
    }

    public Date getPickUpDate() {
        return pickUpDate;
    }

    public void setPickUpDate(Date pickUpDate) {
        this.pickUpDate = pickUpDate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TicketDTO)) return false;
        TicketDTO ticketDTO = (TicketDTO) o;
        return  Objects.equals(userId, ticketDTO.userId) &&
                playId.equals(ticketDTO.playId) &&
                status == ticketDTO.status &&
                Objects.equals(bookDate, ticketDTO.bookDate) &&
                Objects.equals(pickUpDate, ticketDTO.pickUpDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, playId, status, bookDate, pickUpDate);
    }
}
