package com.blue.tnb.notification;

import com.blue.tnb.model.Play;
import com.blue.tnb.service.PlayService;
import com.blue.tnb.service.PlayServiceImpl;
import com.blue.tnb.service.TicketServiceImpl;
import com.blue.tnb.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class Notification {

    @Autowired
    private PlayServiceImpl playService;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private TicketServiceImpl ticketService;

    Notification(){}

    //Luni - 12:00 notificare toate biletele de book pentru maine ora 14:00
    //Marti - notificare cu o ora inainte de available
    //Joi - ridicare bilete pana in ora 16:00 - notificare pt pickup = null

    //JobReminder - verificare bilete today+1 cron = 0 0 12 * * *
    //BookReminder - verificare bilete today cron = 0 0 13 * * *
    //PickupReminder - verificare bilete fara pickup cron = 0 16 * * THU
    //EndpointReminder - send notificari pt bilete noi

    //0 0 12 * * * || 0/10 * * * * *
    @Scheduled(cron = "0 0 12 * * *")
    public void OneDayAheadBookReminder(){
        List<String> emails = userDetailsService.getAllSubscribersThatCanBookTickets();
        List<Play> plays = playService.getNextAvailablePlays(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));

    }

    //0 0 13 * * * || 0/10 * * * * *
    @Scheduled(cron = "0 0 13 * * *")
    public void OneHourAheadBookReminder(){
        List<String> emails = userDetailsService.getAllSubscribersThatCanBookTickets();
        List<Play> plays = playService.getNextAvailablePlays(LocalDateTime.now(), LocalDateTime.now().plusDays(1));

    }

    //0 16 * * THU  every thursday 0/10 * * * * *
    @Scheduled(cron = "0 16 * * THU")
    public void pickupReminder(){
        List<String> concatEmailsAndPlaysList = ticketService.findEmailsForTicketStatusBooked();
        List<String[]> splitEmailsAndPlaysList = new ArrayList<>();

        for(String row : concatEmailsAndPlaysList){
            splitEmailsAndPlaysList.add(row.split("\\|\\|"));
        }
        splitEmailsAndPlaysList.forEach(element -> System.out.println(Arrays.toString(element)));

    }

}
