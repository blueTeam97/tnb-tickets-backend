package com.blue.tnb;

import com.blue.tnb.validator.TicketValidator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TicketValidatorTest {
    @Autowired
    TicketValidator ticketValidator;

    @Test
    public void testCase1(){
        String date1="1960-01-22 23:11:00";
        String date2="0-00-00 0:0:0";
        String date3="2020-32-32 12:11:11";
        String date4="2020-32-32 12:1111:11";

        assertFalse(ticketValidator.validateTicketDate(date1));
        assertFalse(ticketValidator.validateTicketDate(date2));
        assertFalse(ticketValidator.validateTicketDate(date3));
        assertFalse(ticketValidator.validateTicketDate(date4));
    }
}
