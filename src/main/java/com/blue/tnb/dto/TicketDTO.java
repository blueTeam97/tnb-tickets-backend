
package com.blue.tnb.dto;

import com.blue.tnb.constants.Status;
import com.blue.tnb.model.Ticket;
import java.util.Date;
import java.util.Objects;
import javax.validation.constraints.NotEmpty;

public class TicketDTO {
    @NotEmpty(message = "A ticket MUST have an ID associated")

    private Long id;

    private Long userId;

    @NotEmpty(message = "A ticket MUST have a play associated")

    private Long playId;

    private String status;

    private Date bookDate;

    private Date pickUpDate;

    public TicketDTO() {}

    public TicketDTO(Ticket ticket) {
        this.id = ticket.getId();
        this.playId = ticket.getPlayId();
        this.userId = ticket.getUserId();
        this.status = ticket.getStatus().getValue();
        this.bookDate = ticket.getBookDate();
        this.pickUpDate = ticket.getPickUpDate();
    }

    public TicketDTO(Long userId, Long playId, Date bookDate, Date pickUpDate) {
        this.userId = userId;
        this.playId = playId;
        this.bookDate = bookDate;
        this.pickUpDate = pickUpDate;
        this.status = Status.FREE.getValue();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPlayId() {
        return this.playId;
    }

    public void setPlayId(Long playId) {
        this.playId = playId;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status.getValue();
    }

    public Date getBookDate() {
        return this.bookDate;
    }

    public void setBookDate(Date bookDate) {
        this.bookDate = bookDate;
    }

    public Date getPickUpDate() {
        return this.pickUpDate;
    }

    public void setPickUpDate(Date pickUpDate) {
        this.pickUpDate = pickUpDate;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof TicketDTO)) {
            return false;
        } else {
            TicketDTO ticketDTO = (TicketDTO)o;
            return Objects.equals(this.userId, ticketDTO.userId) && this.playId.equals(ticketDTO.playId) && this.status == ticketDTO.status && Objects.equals(this.bookDate, ticketDTO.bookDate) && Objects.equals(this.pickUpDate, ticketDTO.pickUpDate);
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.userId, this.playId, this.status, this.bookDate, this.pickUpDate});
    }
}
