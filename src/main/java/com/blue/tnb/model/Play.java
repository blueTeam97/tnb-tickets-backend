package com.blue.tnb.model;

import com.blue.tnb.dto.PlayDTO;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity
public class Play {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @Size(max = 255)
    private String playName;

    @Column(name ="available_date")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date availableDate;

    @Column(name ="play_date")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date playDate;

    @Column(name ="registered_date")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date registeredDate;

    @Column(name = "link")
    @Size(max = 255)
    private String link;

    @Column(name ="nr_tickets")
    private int ticketsNumber;

    @OneToMany(mappedBy = "play", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Ticket> ticketList;

    public Play() {}

    public Play(PlayDTO playDTO) {
        this.setId(playDTO.getId());
        this.setAvailableDate(playDTO.getAvailableDate());
        this.setLink(playDTO.getLink());
        this.setPlayDate(playDTO.getPlayDate());
        this.setRegisteredDate(playDTO.getRegisteredDate());
        this.setPlayName(playDTO.getPlayName());
        this.setTicketsNumber(playDTO.getTicketsNumber());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlayName() {
        return playName;
    }

    public void setPlayName(String playName) {
        this.playName = playName;
    }

    public Date getAvailableDate() {
        return availableDate;
    }

    public void setAvailableDate(Date availableDate) {
        this.availableDate = availableDate;
    }

    public Date getPlayDate() {
        return playDate;
    }

    public void setPlayDate(Date playDate) {
        this.playDate = playDate;
    }

    public Date getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(Date registeredDate) {
        this.registeredDate = registeredDate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getTicketsNumber() {
        return ticketsNumber;
    }

    public void setTicketsNumber(int ticketsNumber) {
        this.ticketsNumber = ticketsNumber;
    }


    @Override
    public String toString() {
        return "Play{" +
                "id=" + id +
                ", playName='" + playName + '\'' +
                ", availableDate=" + availableDate +
                ", playDate=" + playDate +
                ", registeredDate=" + registeredDate +
                ", link='" + link + '\'' +
                ", ticketsNumber=" + ticketsNumber +
                '}';
    }
}