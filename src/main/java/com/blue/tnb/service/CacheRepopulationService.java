package com.blue.tnb.service;

import com.blue.tnb.constants.Status;
import com.blue.tnb.model.Pair;
import com.blue.tnb.model.Play;
import com.blue.tnb.model.Ticket;
import com.blue.tnb.model.User;
import com.blue.tnb.repository.PlayRepository;
import com.blue.tnb.repository.TicketRepository;
import com.blue.tnb.repository.UserRepository;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CacheRepopulationService {

    @Autowired
    private HazelcastInstance hazelcastInstance;

    @Autowired
    PlayRepository playRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TicketRepository ticketRepository;

    @PostConstruct
    public void RepopulateCache(){

        //IMap<Long,IMap<Map<Long,List<Long>>, Map<Pair<Long,String>,Long>>> map=hazelcastInstance.getMap("availableTickets");
        IMap<Long,List<Long>> map=hazelcastInstance.getMap("availableTickets");
        IMap<Long,LocalDateTime> userMap=hazelcastInstance.getMap("userTickets");
        List<Play> plays=playRepository.findAllNoRestriction();
        plays.stream().forEach(play->{
            map.put(play.getId(),play.getTicketList().stream()
                                                        .filter(ticket-> ticket.getStatus().equals(Status.FREE))
                                                        .map(Ticket::getId)
                                                        .collect(Collectors.toList()));
        });
        List<User> users=userRepository.findAll();
        users.stream().forEach(user->{
            userMap.put(user.getId(),user.getLastBook()==null?LocalDateTime.now().minusDays(32):user.getLastBook());
        });
    }
}
