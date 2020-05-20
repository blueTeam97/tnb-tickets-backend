package com.blue.tnb.controller;

import com.blue.tnb.dto.TicketDTO;
import com.blue.tnb.service.TicketServiceImpl;
import org.hibernate.annotations.GeneratorType;
import org.springframework.beans.factory.annotation.Autowired;
import com.blue.tnb.model.User;
import com.blue.tnb.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class TestController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @GetMapping("/all")
    public List<String> getAllSubscribers() {
       return userDetailsService.getAllSubscribersThatCanBookTickets();
    }

}
