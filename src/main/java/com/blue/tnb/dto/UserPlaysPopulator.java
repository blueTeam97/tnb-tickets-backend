package com.blue.tnb.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserPlaysPopulator {

    List<PlayDTO> userEdiblePlays;
    TicketDTO userLastBookedTicket;
    List<PlayDTO> bookedAvailablePlays;

    public UserPlaysPopulator(){
        userEdiblePlays=new ArrayList<>();
        bookedAvailablePlays=new ArrayList<>();
    }

    public void addBookedAvailablePlayDTO(PlayDTO playDTO){bookedAvailablePlays.add(playDTO);}

    public void addPlayDTO(PlayDTO playDTO){
        userEdiblePlays.add(playDTO);
    }

    public List<PlayDTO> getUserEdiblePlays() {
        return userEdiblePlays;
    }

    public void setUserEdiblePlays(List<PlayDTO> userEdiblePlays) {
        this.userEdiblePlays = userEdiblePlays;
    }

    public TicketDTO getUserLastBookedTicket() {
        return userLastBookedTicket;
    }

    public void setUserLastBookedTicket(TicketDTO userLastBookedTicket) {
        this.userLastBookedTicket = userLastBookedTicket;
    }

    public List<PlayDTO> getBookedAvailablePlays() {
        return bookedAvailablePlays;
    }

    public void setBookedAvailablePlays(List<PlayDTO> bookedAvailablePlays) {
        this.bookedAvailablePlays = bookedAvailablePlays;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserPlaysPopulator)) return false;

        UserPlaysPopulator that = (UserPlaysPopulator) o;

        if (!Objects.equals(userEdiblePlays, that.userEdiblePlays))
            return false;
        return Objects.equals(userLastBookedTicket, that.userLastBookedTicket);
    }

    @Override
    public int hashCode() {
        int result = userEdiblePlays != null ? userEdiblePlays.hashCode() : 0;
        result = 31 * result + (userLastBookedTicket != null ? userLastBookedTicket.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserPlaysPopulator{" +
                "userEdiblePlays=" + userEdiblePlays +
                ", userLastBookedTicket=" + userLastBookedTicket +
                '}';
    }
}
