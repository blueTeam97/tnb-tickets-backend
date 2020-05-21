package com.blue.tnb.notification;

import com.blue.tnb.model.Play;
import com.blue.tnb.service.PlayServiceImpl;
import com.blue.tnb.service.TicketServiceImpl;
import com.blue.tnb.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class Notification {

    @Autowired
    private PlayServiceImpl playService;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private TicketServiceImpl ticketService;

    @Autowired
    private  Environment env;

    @Autowired
    private  JavaMailSender mailSender;

    Notification(){
    }

    //Luni - 12:00 notificare toate biletele de book pentru maine ora 14:00
    //Marti - notificare cu o ora inainte de available
    //Joi - ridicare bilete pana in ora 16:00 - notificare pt pickup = null

    //JobReminder - verificare bilete today+1 cron = 0 0 12 * * *
    //BookReminder - verificare bilete today cron = 0 0 13 * * *
    //PickupReminder - verificare bilete fara pickup cron = 0 16 * * THU
    //EndpointReminder - send notificari pt bilete noi

    //0 0 12 * * * || 0/10 * * * * *
    @Scheduled(cron = "0 0 12 * * *")
    public void oneDayAheadBookReminder(){
        List<String> emails = userDetailsService.getAllSubscribersThatCanBookTickets();
        List<Play> plays = playService.getNextAvailablePlays(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));
        String subject = "TNB tickets available tomorrow";
        //StringBuilder message = new StringBuilder("Tomorrow the following tickets will become available to be booked: \n\n");
        String typeOfEmail = "One day before";
        constructAndSendEmail(emails, plays,subject,typeOfEmail);
    }

    //0 0 13 * * * || 0/10 * * * * *
    @Scheduled(cron = "0 0 13 * * *")
    public void oneHourAheadBookReminder(){
        List<String> emails = userDetailsService.getAllSubscribersThatCanBookTickets();
        List<Play> plays = playService.getNextAvailablePlays(LocalDate.now(), LocalDate.now().plusDays(1));
        String subject = "TNB tickets available today";
        //StringBuilder message = new StringBuilder("There is one hour left until the tickets become available: \n\n");
        String typeOfEmail = "One hour before";
        constructAndSendEmail(emails, plays,subject,typeOfEmail);
    }

    //0 0 16 * * THU  every thursday 0/10 * * * * *
    @Scheduled(cron = "0 0 16 * * THU")
    public void pickupReminder(){
        List<String> concatEmailsAndPlaysList = ticketService.findEmailsForTicketStatusBooked();
        List<String[]> splitEmailsAndPlaysList = new ArrayList<>();

        for(String row : concatEmailsAndPlaysList){
            splitEmailsAndPlaysList.add(row.split("\\|\\|"));
        }
        //splitEmailsAndPlaysList.forEach(element -> System.out.println(Arrays.toString(element)));
        for(String[] row : splitEmailsAndPlaysList){
            constructAndSendEmail(row[0],row[1],row[2]);
        }
    }

    private final void constructEmailMessage(String emailAddress,String subject,String message) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(emailAddress);
        email.setSubject(subject);
        email.setText(message);
        email.setFrom(Objects.requireNonNull(env.getProperty("support.email")));
    //    mailSender.send(email);
    }

    private void constructAndSendEmail(List<String> emails, List<Play> plays,String subject,String typeOfEmail) {
        StringBuilder message = new StringBuilder();
        for(String email : emails){
            if(typeOfEmail.equals("One day before")){
                message = new StringBuilder();
                message.append("Tomorrow the following tickets will become available to be booked: \n\n");
            }else if(typeOfEmail.equals("One hour before")){
                message = new StringBuilder();
                message.append("There is one hour left until the tickets become available: \n\n");
            }
            for(Play play : plays){
                message.append(play.getPlayName()).append(" on ").append(play.getPlayDate().toString().replace("T", " ")).append('\n');
            }
            // System.out.println("===================================================================================================================");
             constructEmailMessage(email,subject,message.toString());
            // System.out.println("********************************************************************************************************************");
        }
    }

    private void constructAndSendEmail(String email, String playName,String date) {
            String subject = "TNB tickets to pick-up";
            StringBuilder message = new StringBuilder("You should pick-up your tickets for: \n\n");
                message.append(playName).append(" on ").append(date.replace("T", " ")).append('\n');
            // System.out.println("===================================================================================================================");
            constructEmailMessage(email,subject,message.toString());
            //  System.out.println("********************************************************************************************************************");
    }
}
