package com.blue.tnb.model;

import com.blue.tnb.constants.Status;
import com.blue.tnb.dto.TicketDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "play_id",nullable = false)
    private Long playId;

    private Status status;

    @Column(name = "book_date")
    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    private LocalDateTime bookDate;

    @Column(name="pickup_date")
    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    private LocalDateTime pickUpDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "id",nullable = false,insertable = false,updatable = false)
    private Play play;

    public Ticket(Long id, Long userId, Long playId, Status status, LocalDateTime bookDate, LocalDateTime pickUpDate) {
        this.id = id;
        this.userId = userId;
        this.playId = playId;
        this.status = status;
        this.bookDate = bookDate;
        this.pickUpDate = pickUpDate;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getBookDate() {
        return bookDate;
    }

    public void setBookDate(LocalDateTime bookDate) {
        this.bookDate = bookDate;
    }

    public LocalDateTime getPickUpDate() {
        return pickUpDate;
    }

    public void setPickUpDate(LocalDateTime pickUpDate) {
        this.pickUpDate = pickUpDate;
    }

    public Play getPlay() {
        return play;
    }

    public void setPlay(Play play) {
        this.play = play;
    }

    public Ticket(){

    }
    public Ticket(TicketDTO ticketDTO) throws ParseException {
        this.id=ticketDTO.getId();
        this.userId=ticketDTO.getUserId();
        this.playId=ticketDTO.getPlayId();
        if(!StringUtils.isEmpty(ticketDTO.getBookDate())){
            this.bookDate=convertStringToLocalDateTime(ticketDTO.getBookDate());
        }
        else this.bookDate=null;
        if(!StringUtils.isEmpty(ticketDTO.getPickUpDate())){
            this.pickUpDate=convertStringToLocalDateTime(ticketDTO.getPickUpDate());
        }
        else this.pickUpDate=null;
        switch(ticketDTO.getStatus().toLowerCase()){
            case "free":
                this.status=Status.FREE;
                break;
            case "booked":
                this.status=Status.BOOKED;
                break;
            case "pickedup":
                this.status=Status.PICKEDUP;
                break;
        }



    }
    public LocalDateTime convertStringToLocalDateTime(String dateAsString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dateAsString, formatter);
        return dateTime;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(id, ticket.id) &&
                Objects.equals(userId, ticket.userId) &&
                Objects.equals(playId, ticket.playId) &&
                status == ticket.status &&
                Objects.equals(bookDate, ticket.bookDate) &&
                Objects.equals(pickUpDate, ticket.pickUpDate) &&
                Objects.equals(play, ticket.play);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, playId, status, bookDate, pickUpDate);
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", userId=" + userId +
                ", playId=" + playId +
                ", status=" + status +
                ", bookDate=" + bookDate +
                ", pickUpDate=" + pickUpDate +
                ", play=" + play +
                '}';
    }
}