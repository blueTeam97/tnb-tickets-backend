package com.blue.tnb.model;

import com.blue.tnb.constants.Status;
import com.blue.tnb.dto.TicketDTO;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private User user*/;

    public Ticket(Long id, Long userId, Long playId, Status status, Date bookDate, Date pickUpDate) {
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

    public Play getPlay() {
        return play;
    }

    public void setPlay(Play play) {
        this.play = play;
    }

/*    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }*/

    public Ticket(){

    }
    public Ticket(TicketDTO ticketDTO){
        this.id=ticketDTO.getId();
        this.userId=ticketDTO.getUserId();
        this.playId=ticketDTO.getPlayId();
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
        SimpleDateFormat dateTimeFormatter=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        if(StringUtils.isEmpty(ticketDTO.getBookDate())){
            this.bookDate=null;
        }
        else this.bookDate=dateTimeFormatter.parse(ticketDTO.getBookDate());
        if(StringUtils.isEmpty(ticketDTO.getBookDate())){
            this.pickUpDate=null;
        }
        else this.pickUpDate=dateTimeFormatter.parse(ticketDTO.getPickUpDate());

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
