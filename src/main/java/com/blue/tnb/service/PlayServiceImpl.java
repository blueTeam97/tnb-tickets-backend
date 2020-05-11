
package com.blue.tnb.service;

import com.blue.tnb.constants.Status;
import com.blue.tnb.dto.PlayDTO;
import com.blue.tnb.dto.TicketDTO;
import com.blue.tnb.mapper.PlayMapperImpl;
import com.blue.tnb.mapper.TicketMapperImpl;
import com.blue.tnb.model.Play;
import com.blue.tnb.model.Ticket;
import com.blue.tnb.repository.PlayRepository;
import com.blue.tnb.repository.TicketRepository;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("playService")
public class PlayServiceImpl implements PlayService {
    @Autowired
    private PlayRepository playRepository;
    @Autowired
    private PlayMapperImpl playMapperImpl;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private TicketMapperImpl ticketMapperImpl;

    public PlayServiceImpl() {
    }

    public List<PlayDTO> getAllPlays() {
        return playRepository.findAll().stream()
                            .map(playMapperImpl::convertPlayToPlayDTO)
                            .collect(Collectors.toList());


    }

    public PlayDTO getPlayById(Long id) {

        return playRepository.findById(id)
                            .map(playMapperImpl::convertPlayToPlayDTO)
                            .orElseThrow(null);
    }

    public PlayDTO getPlayByName(String playName) {
        return playRepository.findByPlayName(playName)
                            .map(playMapperImpl::convertPlayToPlayDTO)
                            .orElseThrow(null);
    }

    public Play addPlay(PlayDTO playDTO) {
        Play play = this.playRepository.saveAndFlush(this.playMapperImpl.convertPlayDTOToPlay(playDTO));

        for(int i = 0; i < play.getTicketsNumber(); ++i) {
            TicketDTO ticketDTO = new TicketDTO();
            ticketDTO.setPlayId(play.getId());
            ticketDTO.setStatus(Status.FREE);

            Ticket ticket = this.ticketMapperImpl.ticketDTOToTicket(ticketDTO);
            ticket = this.ticketRepository.saveAndFlush(ticket);
        }

        play = this.playRepository.getOne(play.getId());
        return play;
    }

    public Play updatePlay(PlayDTO playDTO, Long id) {
        Play existingPlay = (Play)this.playRepository.getOne(id);
        Play play = this.playMapperImpl.convertPlayDTOToPlay(playDTO);
        existingPlay.setPlayName(play.getPlayName());
        existingPlay.setPlayDate(play.getPlayDate());
        existingPlay.setAvailableDate(play.getAvailableDate());
        existingPlay.setRegisteredDate(play.getRegisteredDate());
        existingPlay.setLink(play.getLink());
        existingPlay.setTicketsNumber(play.getTicketsNumber());
        return (Play)this.playRepository.saveAndFlush(existingPlay);
    }

    public Play deletePlay(Long id) {
        Optional<Play> existingPlay = this.playRepository.findById(id);
        existingPlay.ifPresent((play) -> {
            this.playRepository.delete(play);
        });
        return existingPlay.orElseThrow(null);
    }
}
