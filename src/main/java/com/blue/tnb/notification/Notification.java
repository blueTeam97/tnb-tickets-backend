package com.blue.tnb.notification;

import com.blue.tnb.constants.DayOfWeek;
import com.blue.tnb.exception.WrongDayOfTheWeekException;
import com.blue.tnb.model.Play;
import com.blue.tnb.service.PlayServiceImpl;
import com.blue.tnb.service.TicketServiceImpl;
import com.blue.tnb.service.UserDetailsServiceImpl;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class Notification {

    @Autowired
    private PlayServiceImpl playService;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private TicketServiceImpl ticketService;

    @Autowired
    private Environment env;

    @Autowired
    private JavaMailSender mailSender;

    Notification() {
    }

    //Luni - 12:00 notificare toate biletele de book pentru maine ora 14:00
    //Marti - notificare cu o ora inainte de available
    //Joi - ridicare bilete pana in ora 16:00 - notificare pt pickup = null

    //JobReminder - verificare bilete today+1 cron = 0 0 12 * * *
    //BookReminder - verificare bilete today cron = 0 0 13 * * *
    //PickupReminder - verificare bilete fara pickup cron = 0 16 * * THU
    //EndpointReminder - send notificari pt bilete noi

    //oneDayAheadBookReminder || tenSecondsSchedulerTEST
    @Scheduled(cron = "${tnb.app.notification.oneDayAheadBookReminder}")
    @SchedulerLock(name = "TaskScheduler_oneDayAheadBookReminder",
            lockAtLeastForString = "PT5M", lockAtMostForString = "PT14M")
    public void oneDayAheadBookReminder() {
        System.out.println("IN SCHEDULER");
        List<String> emails = userDetailsService.getAllSubscribersThatCanBookTickets();
        if (emails != null && emails.size() > 0) {
            List<Play> plays = playService.getNextAvailablePlays(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));
            if (plays != null && plays.size() > 0) {
                String typeOfEmail = "One day before";
                constructAndSendEmail(emails, plays, typeOfEmail);
            }
        }
    }

    //oneHourAheadBookReminder || tenSecondsSchedulerTEST
    @Scheduled(cron = "${tnb.app.notification.oneHourAheadBookReminder}")
    @SchedulerLock(name = "TaskScheduler_oneHourAheadBookReminder",
            lockAtLeastForString = "PT5M", lockAtMostForString = "PT14M")
    public void oneHourAheadBookReminder() {
        List<String> emails = userDetailsService.getAllSubscribersThatCanBookTickets();
        if (emails != null && emails.size() > 0) {
            List<Play> plays = playService.getNextAvailablePlays(LocalDate.now(), LocalDate.now().plusDays(1));
            if (plays != null && plays.size() > 0) {
                String typeOfEmail = "One hour before";
                constructAndSendEmail(emails, plays, typeOfEmail);
            }
        }
    }

    //thursdayAndFridayPickupReminder || tenSecondsSchedulerTEST
    @Scheduled(cron = "${tnb.app.notification.thursdayAndFridayPickupReminder}")
    @SchedulerLock(name = "TaskScheduler_thursdayAndFridayPickupReminder",
            lockAtLeastForString = "PT5M", lockAtMostForString = "PT14M")
    public void thursdayAndFridayPickupReminder() throws WrongDayOfTheWeekException {

        if (LocalDate.now().getDayOfWeek().getValue() == DayOfWeek.THURSDAY.getValue()) {
            ticketService.freeBookedTicketsOncePlayDateInThePast();
        }

        List<String> concatEmailsAndPlaysList = ticketService.findEmailsForTicketStatusBooked();
        if (concatEmailsAndPlaysList != null) {
            List<String[]> splitEmailsAndPlaysList = new ArrayList<>();

            for (String row : concatEmailsAndPlaysList) {
                splitEmailsAndPlaysList.add(row.split("\\|\\|"));
            }
            //splitEmailsAndPlaysList.forEach(element -> System.out.println(Arrays.toString(element)));
            for (String[] row : splitEmailsAndPlaysList) {
                constructAndSendEmail(row[0], row[1], row[2]);
            }
        }
    }

    //fridayChangeStatus || tenSecondsSchedulerTEST
    @Scheduled(cron = "${tnb.app.notification.fridayChangeStatus}")
    @SchedulerLock(name = "TaskScheduler_fridayChangeStatus",
            lockAtLeastForString = "PT5M", lockAtMostForString = "PT14M")
    public void fridayChangeStatus() {

        List<Play> plays = playService.getPlaysWhereTicketsStatusFromBookToFree();
        if (plays != null) {
            List<String> emails = userDetailsService.getAllSubscribersThatCanBookTickets();
            ticketService.updateTicketStatusToFree();
            if (emails != null) {
                String message = "Available tickets";
                constructAndSendEmail(emails, plays, message);
            }
        }
    }


//    private final void constructEmailMessage(String emailAddress, String subject, String message) {
//        final SimpleMailMessage email = new SimpleMailMessage();
//        email.setTo(emailAddress);
//        email.setSubject(subject);
//        email.setText(message);
////        email.setFrom(Objects.requireNonNull(env.getProperty("support.email")));
//        email.setFrom("TNBTickets");
//        mailSender.send(email);
//
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println("EMAIL SENT TO:    " + emailAddress);
//    }


    public void constructAndSendEmail(List<String> emails, List<Play> plays, String typeOfEmail) {
        StringBuilder message = new StringBuilder();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        String subject = new String();
        for (String email : emails) {
            if (typeOfEmail.equals("One day before")) {
                subject = "TNB tickets available tomorrow";
                message = new StringBuilder();
                message.append("Tomorrow the following tickets will become available to be booked: \n\n");
            } else if (typeOfEmail.equals("One hour before")) {
                subject = "TNB tickets available today";
                message = new StringBuilder();
                message.append("There is one hour left until the tickets become available: \n\n");
            } else if (typeOfEmail.equals("Available tickets")) {
                subject = "TNB tickets available now";
                message = new StringBuilder();
                message.append("There are tickets available for the following play: \n\n");
            }
            for (Play play : plays) {
                String date = removeSecondsFromDate(play.getPlayDate().toString());
                message.append(play.getPlayName()).append(" on ").append(date).append('\n');
            }
            System.out.println("===================================================================================================================");
            System.out.println(email);
            executorService.execute(new NotificationEmail(subject, message.toString(), email, mailSender));
            System.out.println("********************************************************************************************************************");
        }
        try {
            executorService.shutdown();
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

//    @Async("emailTaskExecutor")
//    private void sendEmail(String subject, String message, String mail) {
//        final SimpleMailMessage email = new SimpleMailMessage();
//        email.setTo(mail);
//        email.setSubject(subject);
//        email.setText(message);
//        email.setFrom(Objects.requireNonNull(env.getProperty("support.email")));
////        email.setFrom("TNBTickets");
////        mailSender.send(email);
//        System.out.println("EMAIL SENT TO:    " + Thread.currentThread().getName());
//        try {
//            Thread.sleep(1000);
//            System.out.println("Email was sent" );
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }


    private String removeSecondsFromDate(String _date) {
        String date = _date.replace("T", " ");
        int count = StringUtils.countOccurrencesOf(date, ":");
        String dateWithoutSeconds;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("EEEE, MMM dd yyyy 'at' HH:mm");
        LocalDateTime formatDateTime;

        if (count == 2) {
            dateWithoutSeconds = date.substring(0, date.lastIndexOf(":"));
            formatDateTime = LocalDateTime.parse(dateWithoutSeconds, formatter);
            return formatDateTime.format(outputFormat);
        }
        formatDateTime = LocalDateTime.parse(date, formatter);
        return formatDateTime.format(outputFormat);
    }


    private void constructAndSendEmail(String email, String playName, String date) throws WrongDayOfTheWeekException {
        String subject = "TNB tickets to pick-up";
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        StringBuilder message;
        if (LocalDate.now().getDayOfWeek().getValue() == DayOfWeek.THURSDAY.getValue()) {
            message = new StringBuilder("You should pick-up your tickets for: \n\n");

        } else if (LocalDate.now().getDayOfWeek().getValue() == DayOfWeek.FRIDAY.getValue()) {
            message = new StringBuilder("Hurry up! You have one hour left to pick-up your tickets for: \n\n");
        } else {
            throw new WrongDayOfTheWeekException("Wrong day of the week to pick-up tickets!");
        }
        message.append(playName).append(" on ").append(removeSecondsFromDate(date)).append('\n');
        System.out.println("===================================================================================================================");
        executorService.execute(new NotificationEmail(subject, message.toString(), email, mailSender));
        System.out.println("********************************************************************************************************************");
        try {
            executorService.shutdown();
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
