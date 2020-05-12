package com.blue.tnb.dto;

import com.blue.tnb.mapper.TicketMapperImpl;
import com.blue.tnb.model.Play;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.blue.tnb.model.Ticket;
import lombok.*;
import org.apache.tomcat.jni.Local;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class PlayDTO {

    private Long id;

    @NotEmpty(message = "Play must have a name specified")
    private String playName;

    @NotNull(message = "Insert Date")
    private String availableDate;

    @NotNull(message = "Insert Date")
    private String playDate;

    private String registeredDate;

    @NotEmpty(message = "Play must have a link")
    private String link;

    @NotNull(message = "Specify the number of tickets for this play")
    @Min(value = 1)
    private int ticketsNumber;
    List<TicketDTO> ticketDTOList;

    private Long availableTicketsNumber= 0L;
    List<TicketDTO> ticketList;

    public PlayDTO() {}

    public PlayDTO(Play play) {
        this.id = play.getId();
        this.playName = play.getPlayName();
        this.link = play.getLink();
        this.ticketsNumber = play.getTicketsNumber();
        this.availableTicketsNumber=0L;

        this.playDate = play.getPlayDate().toString();
        this.availableDate = play.getAvailableDate().toString();
        this.registeredDate = play.getRegisteredDate().toString();

        TicketMapperImpl ticketMapper = new TicketMapperImpl();
        this.ticketDTOList = ticketMapper.convertTicketToTicketDTOList(play.getTicketList());

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


    public List<TicketDTO> getTicketDTOList() {
        return this.ticketDTOList;
    }

    public void setTicketDTOList(List<TicketDTO> ticketDTOList) {
        this.ticketDTOList = ticketDTOList;
    }

    public void addTicketDTO(TicketDTO ticketDTO) {
        System.out.println(ticketDTO);
        this.ticketDTOList.add(ticketDTO);
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

    public String toString() {
        return "PlayDTO(id=" + this.getId() + ", playName=" + this.getPlayName() + ", availableDate=" + this.getAvailableDate() + ", playDate=" + this.getPlayDate() + ", registeredDate=" + this.getRegisteredDate() + ", link=" + this.getLink() + ", ticketsNumber=" + this.getTicketsNumber() + ", ticketDTOList=" + this.getTicketDTOList() + ")";
    }
}
