package com.blue.tnb.model;

import com.blue.tnb.constants.Status;
import com.blue.tnb.dto.TicketDTO;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
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
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date bookDate;

    @Column(name="pickup_date")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date pickUpDate;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private Play play;

   /* @ManyToOne
    @JoinColumn(name="id")
    private User user;*/

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



    public Ticket(){

    }
    public Ticket(TicketDTO ticketDTO){
        this.id=ticketDTO.getId();
        this.userId=ticketDTO.getUserId();
        this.playId=ticketDTO.getPlayId();
        switch(ticketDTO.getStatus()){
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
        this.bookDate=ticketDTO.getBookDate();
        this.pickUpDate=ticketDTO.getPickUpDate();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ticket)) return false;
        Ticket ticket = (Ticket) o;
        return id.equals(ticket.id) &&
                Objects.equals(userId, ticket.userId) &&
                playId.equals(ticket.playId) &&
                status == ticket.status &&
                Objects.equals(bookDate, ticket.bookDate) &&
                Objects.equals(pickUpDate, ticket.pickUpDate);
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
                '}';
    }
}