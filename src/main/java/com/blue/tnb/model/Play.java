package com.blue.tnb.model;

import com.blue.tnb.constants.DateUtils;
import com.blue.tnb.dto.PlayDTO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.tomcat.jni.Local;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Objects;
@Entity
public class Play {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @Size(max = 255)
    @NotEmpty
    private String playName;

    @Column(name = "available_date")
    @NotNull
    private LocalDateTime availableDate;

    @Column(name = "play_date")
    @NotNull
    private LocalDateTime playDate;

    @Column(name = "registered_date")
    @CreationTimestamp
    private LocalDateTime registeredDate;

    @Column(name = "link")
    @Size(max = 255)
    @NotEmpty
    private String link;

    @Column(name = "nr_tickets")
    @Min(value = 1)
    @NotNull
    private int ticketsNumber;

    @OneToMany(mappedBy = "play", fetch = FetchType.EAGER,  cascade = CascadeType.ALL) //orphanRemoval = true
    private List<Ticket> ticketList;

    public Play() {}

    public Play(PlayDTO playDTO) {
        this.setLink(playDTO.getLink());
        this.setPlayName(playDTO.getPlayName());
        this.setTicketsNumber(playDTO.getTicketsNumber());
        this.setPlayDate(convertStringToLocalDateTime(playDTO.getPlayDate()));
        this.setAvailableDate(convertStringToLocalDateTime(playDTO.getAvailableDate()));
    }

    public LocalDateTime convertStringToLocalDateTime(String dateAsString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dateAsString, formatter);
        return dateTime;
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

    public LocalDateTime getAvailableDate() {
        return this.availableDate;
    }

    public void setAvailableDate(LocalDateTime availableDate) {
        this.availableDate = availableDate;
    }

    public LocalDateTime getPlayDate() {
        return this.playDate;
    }

    public void setPlayDate(LocalDateTime playDate) {
        this.playDate = playDate;
    }

    public LocalDateTime getRegisteredDate() {
        return this.registeredDate;
    }

    public void setRegisteredDate(LocalDateTime registeredDate) {
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

    @Override
    public int hashCode() {
        return Objects.hash(id, playName, availableDate, playDate, registeredDate, link, ticketsNumber, ticketList);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Play)) return false;
        Play play = (Play) o;
        return ticketsNumber == play.ticketsNumber &&
                id.equals(play.id) &&
                playName.equals(play.playName) &&
                availableDate.equals(play.availableDate) &&
                playDate.equals(play.playDate) &&
                registeredDate.equals(play.registeredDate) &&
                link.equals(play.link) &&
                Objects.equals(ticketList, play.ticketList);
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
