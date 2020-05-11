package com.blue.tnb.model;

import com.blue.tnb.constants.Status;
import com.blue.tnb.dto.TicketDTO;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Ticket {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    @Column(
            name = "user_id"
    )
    private Long userId;
    @Column(
            name = "play_id",
            nullable = false
    )
    private Long playId;
    private Status status;
    @Column(
            name = "book_date"
    )
    @Temporal(TemporalType.TIMESTAMP)
    private Date bookDate;
    @Column(
            name = "pickup_date"
    )
    @Temporal(TemporalType.TIMESTAMP)
    private Date pickUpDate;
    @ManyToOne
    @JoinColumn(
            name = "play_id",
            referencedColumnName = "id",
            nullable = false,
            insertable = false,
            updatable = false
    )
    private Play play;

    public Play getPlay() {
        return this.play;
    }

    public void setPlay(Play play) {
        this.play = play;
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

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

    public Ticket() {
    }

    public Ticket(TicketDTO ticketDTO) {
        this.id = ticketDTO.getId();
        this.userId = ticketDTO.getUserId();
        this.playId = ticketDTO.getPlayId();
        String var2 = ticketDTO.getStatus();
        byte var3 = -1;
        switch(var2.hashCode()) {
            case -1383386808:
                if (var2.equals("booked")) {
                    var3 = 1;
                }
                break;
            case -738920677:
                if (var2.equals("pickedup")) {
                    var3 = 2;
                }
                break;
            case 3151468:
                if (var2.equals("free")) {
                    var3 = 0;
                }
        }

        switch(var3) {
            case 0:
                this.status = Status.FREE;
                break;
            case 1:
                this.status = Status.BOOKED;
                break;
            case 2:
                this.status = Status.PICKEDUP;
        }

        this.bookDate = ticketDTO.getBookDate();
        this.pickUpDate = ticketDTO.getPickUpDate();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof Ticket)) {
            return false;
        } else {
            Ticket ticket = (Ticket)o;
            return this.id.equals(ticket.id) && Objects.equals(this.userId, ticket.userId) && this.playId.equals(ticket.playId) && this.status == ticket.status && Objects.equals(this.bookDate, ticket.bookDate) && Objects.equals(this.pickUpDate, ticket.pickUpDate);
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.id, this.userId, this.playId, this.status, this.bookDate, this.pickUpDate});
    }

    public String toString() {
        return "Ticket{id=" + this.id + ", userId=" + this.userId + ", playId=" + this.playId + ", status=" + this.status + ", bookDate=" + this.bookDate + ", pickUpDate=" + this.pickUpDate + '}';
    }
}
