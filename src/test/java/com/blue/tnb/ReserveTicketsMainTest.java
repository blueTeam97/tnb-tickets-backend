package com.blue.tnb;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ReserveTicketsMainTest {

    private ExecutorService executor= Executors.newFixedThreadPool(5);

    @Test
    public void testCase(){
        ArrayList<String> userIds=new ArrayList<String>(Arrays.asList("1", "2", "3", "4", "5"));
        System.out.println("Begin test transactions");
        List<Future<String>> results=new ArrayList<>(5);
        userIds.stream().forEach(u->{
           Future<String> result =  executor.submit(new TicketBookingTest(u));
           results.add(result);
        });
        results.stream().forEach(var-> {
            try {
                System.out.println(var.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
        System.out.println("End of test");
    }
}
