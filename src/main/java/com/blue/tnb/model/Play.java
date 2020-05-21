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
import javax.validation.constraints.*;

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
    @NotBlank
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
    @NotBlank
    private String link;

    @Column(name = "nr_tickets")
    @Min(value = 1)
    @NotNull
    private int ticketsNumber;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(mappedBy = "play", fetch = FetchType.EAGER,  cascade = CascadeType.ALL)
    private List<Ticket> ticketList;

    public Play() {}

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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Play play = (Play) o;
        return ticketsNumber == play.ticketsNumber &&
                Objects.equals(id, play.id) &&
                Objects.equals(playName, play.playName) &&
                Objects.equals(availableDate, play.availableDate) &&
                Objects.equals(playDate, play.playDate) &&
                Objects.equals(registeredDate, play.registeredDate) &&
                Objects.equals(link, play.link) &&
                Objects.equals(imageUrl, play.imageUrl) &&
                Objects.equals(ticketList, play.ticketList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, playName, availableDate, playDate, registeredDate, link, ticketsNumber, imageUrl, ticketList);
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
                ", imageUrl='" + imageUrl + '\'' +
                ", ticketList=" + ticketList +
                '}';
    }
}
