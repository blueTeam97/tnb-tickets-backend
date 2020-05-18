package com.blue.tnb.dto;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Objects;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class PlayDTO {

    private Long id;

    @NotBlank(message = "Play must have a name specified")
    private String playName;

    @NotNull(message = "Insert Date")
    private String availableDate;

    @NotNull(message = "Insert Date")
    private String playDate;

    private String registeredDate;

    @NotBlank(message = "Play must have a link")
    private String link;

    @NotNull(message = "Specify the number of tickets for this play")
    @Min(value = 2)
    private int ticketsNumber;

    @JsonIgnore
    List<TicketDTO> ticketDTOList;

    private Long availableTicketsNumber = 0L;
    private Long bookedTicketsNumber = 0L;


    public PlayDTO() {}


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

    public String getAvailableDate() {
        return availableDate;
    }

    public void setAvailableDate(String availableDate) {
        this.availableDate = availableDate;
    }

    public String getPlayDate() {
        return this.playDate;
    }

    public void setPlayDate(String playDate) {
        this.playDate = playDate;
    }

    public String getRegisteredDate() {
        return this.registeredDate;
    }

    public void setRegisteredDate(String registeredDate) {
        this.registeredDate = registeredDate;
    }

    public String getLink() {
        return this.link;
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

    public Long getBookedTicketsNumber() {
        return bookedTicketsNumber;
    }

    public void setBookedTicketsNumber(Long bookedTicketsNumber) {
        this.bookedTicketsNumber = bookedTicketsNumber;
    }

    public List<TicketDTO> getTicketDTOList() {
        return this.ticketDTOList;
    }

    public void setTicketDTOList(List<TicketDTO> ticketDTOList) {
        this.ticketDTOList = ticketDTOList;
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
                Objects.equals(registeredDate, playDTO.registeredDate) &&
                Objects.equals(link, playDTO.link) &&
                Objects.equals(ticketDTOList, playDTO.ticketDTOList) &&
                Objects.equals(availableTicketsNumber, playDTO.availableTicketsNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, playName, availableDate, playDate, registeredDate, link, ticketsNumber, ticketDTOList, availableTicketsNumber);
    }

    @Override
    public String toString() {
        return "PlayDTO{" +
                "id=" + id +
                ", playName='" + playName + '\'' +
                ", availableDate='" + availableDate + '\'' +
                ", playDate='" + playDate + '\'' +
                ", registeredDate='" + registeredDate + '\'' +
                ", link='" + link + '\'' +
                ", ticketsNumber=" + ticketsNumber +
                ", ticketDTOList=" + ticketDTOList +
                ", availableTicketsNumber=" + availableTicketsNumber +
                '}';
    }
}
