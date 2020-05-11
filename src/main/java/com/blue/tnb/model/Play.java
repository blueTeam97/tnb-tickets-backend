//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.blue.tnb.model;

import com.blue.tnb.dto.PlayDTO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;

@Entity
public class Play {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @Size(max = 255)
    private String playName;

    @Column(name = "available_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date availableDate;

    @Column(name = "play_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date playDate;

    @Column(name = "registered_date")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date registeredDate;

    @Column(name = "link")
    @Size(max = 255)
    private String link;

    @Column(name = "nr_tickets")
    @Min(value = 1)
    @NotNull
    private int ticketsNumber;
    @OneToMany(
            mappedBy = "play",
            fetch = FetchType.EAGER,
            cascade = {CascadeType.ALL}
    )
    private List<Ticket> ticketList = new ArrayList();

    public Play() {}

    public Play(PlayDTO playDTO) {
        this.setLink(playDTO.getLink());
        this.setPlayName(playDTO.getPlayName());
        this.setTicketsNumber(playDTO.getTicketsNumber());

        try {
            SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            this.setPlayDate(dateTimeFormatter.parse(playDTO.getPlayDate()));
            this.setAvailableDate(dateTimeFormatter.parse(playDTO.getAvailableDate()));
        } catch (ParseException var3) {
            var3.printStackTrace();
        }

    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlayName() {
        return this.playName;
    }

    public void setPlayName(String playName) {
        this.playName = playName;
    }

    public Date getAvailableDate() {
        return this.availableDate;
    }

    public void setAvailableDate(Date availableDate) {
        this.availableDate = availableDate;
    }

    public Date getPlayDate() {
        return this.playDate;
    }

    public void setPlayDate(Date playDate) {
        this.playDate = playDate;
    }

    public Date getRegisteredDate() {
        return this.registeredDate;
    }

    public void setRegisteredDate(Date registeredDate) {
        this.registeredDate = registeredDate;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getTicketsNumber() {
        return this.ticketsNumber;
    }

    public void setTicketsNumber(int ticketsNumber) {
        this.ticketsNumber = ticketsNumber;
    }

    public List<Ticket> getTicketList() {
        return this.ticketList;
    }

    public void setTicketList(List<Ticket> ticketList) {
        this.ticketList = ticketList;
    }

    public String toString() {
        return "Play{id=" + this.id + ", playName='" + this.playName + '\'' + ", availableDate=" + this.availableDate + ", playDate=" + this.playDate + ", registeredDate=" + this.registeredDate + ", link='" + this.link + '\'' + ", ticketsNumber=" + this.ticketsNumber + ", ticketList=" + this.ticketList + '}';
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Play play = (Play)o;
            return this.ticketsNumber == play.ticketsNumber && Objects.equals(this.id, play.id) && Objects.equals(this.playName, play.playName) && Objects.equals(this.availableDate, play.availableDate) && Objects.equals(this.playDate, play.playDate) && Objects.equals(this.registeredDate, play.registeredDate) && Objects.equals(this.link, play.link) && Objects.equals(this.ticketList, play.ticketList);
        } else {
            return false;
        }
    }

}
