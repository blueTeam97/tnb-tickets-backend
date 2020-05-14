package com.blue.tnb.dto;

import com.blue.tnb.constants.Status;
import com.blue.tnb.mapper.PlayMapperImpl;
import com.blue.tnb.model.Play;
import com.blue.tnb.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotEmpty;
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

    private PlayDTO playDTO;

   public TicketDTO() {}

    public TicketDTO(Ticket ticket) {
        this.id=ticket.getId();
        this.playId=ticket.getPlayId();
        this.userId=ticket.getUserId();
        this.status=ticket.getStatus().getValue();
        if(ticket.getBookDate()==null){
            this.bookDate="";
        }
        else this.bookDate=ticket.getBookDate().toString().replace("T"," ");
        if(ticket.getPickUpDate()==null){
            this.pickUpDate="";
        }
        else this.pickUpDate=ticket.getPickUpDate().toString().replace("T"," ");
        playDTO=new PlayMapperImpl().convertPlayToPlayDTO(ticket.getPlay());
        //playDTO=new PlayDTO(ticket.getPlay().getPlayName(),ticket.getPlay().getPlayDate().toString(),ticket.getPlay().getLink());
    }

    public TicketDTO(@NotEmpty(message = "A ticket MUST have an ID associated") Long id,
                     Long userId,
                     @NotEmpty(message = "A ticket MUST have a play associated") Long playId,
                     String status,
                     String bookDate,
                     String pickUpDate) {
        this.id = id;
        this.userId = userId;
        this.playId = playId;
        this.status = status;
        this.bookDate = bookDate;
        this.pickUpDate = pickUpDate;
    }

    public PlayDTO getPlayDTO() {
        return playDTO;
    }

    public void setPlayDTO(PlayDTO playDTO) {
        this.playDTO = playDTO;
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