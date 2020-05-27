package com.blue.tnb;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ReserveTicketsMainTest {

    private ExecutorService executor= Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @Test
    public void testCase() throws ExecutionException, InterruptedException {
        ArrayList<String> userIds=new ArrayList<String>();
        List<Future<String>> results=new ArrayList<>(20);
        for(int i=1;i<=39;i++) {
            userIds.add(String.valueOf(i));
        }
        LocalDateTime currentTime=LocalDateTime.now();
        userIds.stream().filter(id->Integer.parseInt(id)<18).forEach(u->{
            Future<String> result =  executor.submit(new TicketBookingTest(u,"http://localhost:9081/tasks/play/25/book/"+ u));
            results.add(result);
        });
        userIds.stream().filter(id->Integer.parseInt(id)>=18 && Integer.parseInt(id)<30).forEach(u->{
            Future<String> result =  executor.submit(new TicketBookingTest(u,"http://localhost:9081/tasks/play/22/book/"+ u));
            results.add(result);
        });
        userIds.stream().filter(id->Integer.parseInt(id)>=30).forEach(u->{
            Future<String> result =  executor.submit(new TicketBookingTest(u,"http://localhost:9081/tasks/play/1/book/"+ u));
            results.add(result);
        });

        System.out.println("Begin test transactions");

        results.forEach(res->{
            try {
                System.out.println(res.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
        System.out.println(currentTime.until(LocalDateTime.now(), ChronoUnit.MILLIS));
        System.out.println("End of test");
    }
}
