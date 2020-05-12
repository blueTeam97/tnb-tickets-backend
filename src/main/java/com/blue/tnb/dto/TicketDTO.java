package com.blue.tnb.dto;

import com.blue.tnb.constants.Status;
import com.blue.tnb.model.Ticket;
import javax.validation.constraints.NotEmpty;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class TicketDTO {

    @NotEmpty(message = "A ticket MUST have an ID associated")
    private Long id;

    private Long userId;

    @NotEmpty(message = "A ticket MUST have a play associated")
    private Long playId;

    private String status;

    private String bookDate;

    private String pickUpDate;

   public TicketDTO() {}

    public TicketDTO(Ticket ticket) {
        this.id=ticket.getId();
        this.playId=ticket.getPlayId();
        this.userId=ticket.getUserId();
        this.status=ticket.getStatus().getValue();
        if(ticket.getBookDate()==null){
            this.bookDate="";
        }
        else this.bookDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ticket.getBookDate());

        if(ticket.getPickUpDate()==null){
            this.pickUpDate="";
        }
        else this.pickUpDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ticket.getPickUpDate());
    }

    public TicketDTO(Long userId, Long playId, String bookDate, String pickUpDate) {
        this.userId = userId;
        this.playId = playId;
        this.bookDate = bookDate;
        this.pickUpDate = pickUpDate;
        this.status= Status.FREE.getValue();
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public void setStatus(Status status) {
        this.status = status.getValue();
    }

    public String getBookDate() {
        return bookDate;
    }

    public void setBookDate(String bookDate) {
        this.bookDate = bookDate;
    }

    public String getPickUpDate() {
        return pickUpDate;
    }

    public void setPickUpDate(String pickUpDate) {
        this.pickUpDate = pickUpDate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TicketDTO)) return false;
        TicketDTO ticketDTO = (TicketDTO) o;
        return  Objects.equals(userId, ticketDTO.userId) &&
                playId.equals(ticketDTO.playId) &&
                status.equals(ticketDTO.status) &&
                Objects.equals(bookDate, ticketDTO.bookDate) &&
                Objects.equals(pickUpDate, ticketDTO.pickUpDate);
    }

    @Override
    public String toString() {
        return "TicketDTO{" +
                "id=" + id +
                ", userId=" + userId +
                ", playId=" + playId +
                ", status='" + status + '\'' +
                ", bookDate=" + bookDate +
                ", pickUpDate=" + pickUpDate +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, playId, status, bookDate, pickUpDate);
    }
}