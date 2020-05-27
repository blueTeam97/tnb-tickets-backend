package com.blue.tnb;

import com.blue.tnb.model.Ticket;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class TicketBookingTest implements Callable<String> {

    private final static Logger logger = LoggerFactory.getLogger(TicketBookingTest.class);



    private String userId;

    private String url;

    private RestTemplate restTemplate=new RestTemplate();

    public TicketBookingTest(String userId,String url){
        this.userId=userId;
        this.url=url;
    }

    private String reserveTicket(String userId){

        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization","Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYWR5LmNyb2l0b3Jlc2N1QGdtYWlsLmNvbSIsImlhdCI6MTU5MDU2NDQxMywiZXhwIjoxNTkwNjUwODEzfQ.rT6IyIcrEQbEmS6G1wk-PYdWDtVhAh8m2NsmQXaFBxtztVk1toYo2VDMVQsXdlNsDpxZXZST1LpmJVLOP2wZhA");
        // build the request
        // create a map for post parameters
        HttpEntity request=new HttpEntity(headers);
        try{
            ResponseEntity<String> response = this.restTemplate.exchange(url, HttpMethod.GET,request,String.class,userId);
            return response.getStatusCode()+": "+response.getBody();
        }
        catch(Exception ex){
            return ex.getStackTrace().toString();
        }

    }

    @Override
    public String call() throws Exception {
        return reserveTicket(userId);
    }
}
