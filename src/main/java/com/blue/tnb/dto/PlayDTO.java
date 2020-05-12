package com.blue.tnb.dto;

import com.blue.tnb.model.Play;
import com.blue.tnb.model.Ticket;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Data
public class PlayDTO {

    private Long id;

    @NotEmpty(message = "Play must have a name specified")
    private String playName;

    @NotEmpty(message = "Play must have an available date specified")
    private Date availableDate;

    @NotEmpty(message = "Play must have a valid start date specified")
    private Date playDate;

    private Date registeredDate;

    @NotEmpty(message = "Insert a link")
    private String link;

    @NotEmpty(message = "Specify the number of tickets for this play")
    private int ticketsNumber;

    private Long availableTicketsNumber= 0L;
    List<TicketDTO> ticketList;

    public PlayDTO(Play play) {
        this.id = play.getId();
        this.playName = play.getPlayName();
        this.availableDate = play.getAvailableDate();
        this.playDate = play.getPlayDate();
        this.registeredDate = play.getRegisteredDate();
        this.link = play.getLink();
        this.ticketsNumber = play.getTicketsNumber();
        this.availableTicketsNumber=0L;
    }


    public PlayDTO(Long id,
                   @NotEmpty(message = "Play must have a name specified") String playName,
                   @NotEmpty(message = "Play must have an available date specified") Date availableDate,
                   @NotEmpty(message = "Play must have a valid start date specified") Date playDate,
                   Date registeredDate,
                   @NotEmpty(message = "Insert a link") String link,
                   @NotEmpty(message = "Specify the number of tickets for this play") int ticketsNumber) {
        this.id = id;
        this.playName = playName;
        this.availableDate = availableDate;
        this.playDate = playDate;
        this.registeredDate = registeredDate;
        this.link = link;
        this.ticketsNumber = ticketsNumber;
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

    public Date getRegisteredDate() {return registeredDate;}

    public void setRegisteredDate(Date registeredDate) {this.registeredDate = registeredDate;}

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

    public Long getAvailableTicketsNumber() {
        return availableTicketsNumber;
    }

    public void setAvailableTicketsNumber(Long availableTicketsNumber) {
        this.availableTicketsNumber = availableTicketsNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayDTO playDTO = (PlayDTO) o;
        return ticketsNumber == playDTO.ticketsNumber &&
                Objects.equals(id, playDTO.id) &&
                Objects.equals(playName, playDTO.playName) &&
                Objects.equals(availableDate, playDTO.availableDate) &&
                Objects.equals(playDate, playDTO.playDate) &&
                Objects.equals(link, playDTO.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, playName, availableDate, playDate, link, ticketsNumber);
    }
}
