//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.blue.tnb.dto;

import com.blue.tnb.mapper.TicketMapperImpl;
import com.blue.tnb.model.Play;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class PlayDTO {

    private Long id;

    @NotEmpty(message = "Play must have a name specified")
    private String playName;

    private String availableDate;

    private String playDate;

    private String registeredDate;

    @NotEmpty(message = "Play must have a link")
    private String link;

    @NotNull(message = "Specify the number of tickets for this play")
    private int ticketsNumber;
    List<TicketDTO> ticketDTOList;

    public PlayDTO() {}

    public PlayDTO(Play play) {
        this.id = play.getId();
        this.playName = play.getPlayName();
        this.link = play.getLink();
        this.ticketsNumber = play.getTicketsNumber();
        if (play.getPlayDate() == null) {
            this.playDate = "";
        } else {
            this.playDate = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(play.getPlayDate());
        }

        if (play.getAvailableDate() == null) {
            this.availableDate = "";
        } else {
            this.availableDate = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(play.getAvailableDate());
        }

        if (play.getRegisteredDate() == null) {
            this.registeredDate = "";
        } else {
            this.registeredDate = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(play.getRegisteredDate());
        }

        TicketMapperImpl ticketMapper = new TicketMapperImpl();
        this.ticketDTOList = ticketMapper.convertTicketToTicketDTOList(play.getTicketList());
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

    public String getAvailableDate() {
        return this.availableDate;
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
        return this.ticketsNumber;
    }

    public void setTicketsNumber(int ticketsNumber) {
        this.ticketsNumber = ticketsNumber;
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

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            PlayDTO playDTO = (PlayDTO)o;
            return this.ticketsNumber == playDTO.ticketsNumber && Objects.equals(this.id, playDTO.id) && Objects.equals(this.playName, playDTO.playName) && Objects.equals(this.availableDate, playDTO.availableDate) && Objects.equals(this.playDate, playDTO.playDate) && Objects.equals(this.link, playDTO.link);
        } else {
            return false;
        }
    }

    public String toString() {
        return "PlayDTO(id=" + this.getId() + ", playName=" + this.getPlayName() + ", availableDate=" + this.getAvailableDate() + ", playDate=" + this.getPlayDate() + ", registeredDate=" + this.getRegisteredDate() + ", link=" + this.getLink() + ", ticketsNumber=" + this.getTicketsNumber() + ", ticketDTOList=" + this.getTicketDTOList() + ")";
    }
}
