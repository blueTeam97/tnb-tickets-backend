package com.blue.tnb.dto;

import com.blue.tnb.constants.Status;
import com.blue.tnb.mapper.PlayMapperImpl;
import com.blue.tnb.model.Play;
import com.blue.tnb.model.Ticket;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
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

    private String userEmail;

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

    public String getUserEmail() {
        return userEmail;
    }

    public TicketDTO setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        return this;
    }

    public PlayDTO getPlayDTO() {
        return playDTO;
    }

    public TicketDTO setPlayDTO(PlayDTO playDTO) {

       this.playDTO = playDTO;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public TicketDTO setStatus(String status) {

       this.status = status;
        return this;
    }


    public Long getId() {
        return id;
    }

    public TicketDTO setId(Long id) {

       this.id = id;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public TicketDTO setUserId(Long userId) {

       this.userId = userId;
        return this;
    }

    public Long getPlayId() {
        return playId;
    }

    public TicketDTO setPlayId(Long playId) {

       this.playId = playId;
        return this;
    }

    public TicketDTO setStatus(Status status) {
        this.status = status.getValue();
        return this;
    }

    public String getBookDate() {
        return bookDate;
    }

    public TicketDTO setBookDate(LocalDateTime bookDate) {
        if(bookDate==null){
            this.bookDate="";
        }
        else this.bookDate=bookDate.toString().replace("T"," ");

        return this;
    }

    public String getPickUpDate() {
        return pickUpDate;
    }

    public TicketDTO setPickUpDate(LocalDateTime pickUpDate) {
        if(pickUpDate==null){
            this.pickUpDate="";
        }
        else this.pickUpDate = pickUpDate.toString().replace("T"," ");

        return this;
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