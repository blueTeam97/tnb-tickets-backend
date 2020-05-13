
package com.blue.tnb.service;

import com.blue.tnb.constants.Status;
import com.blue.tnb.dto.PlayDTO;
import com.blue.tnb.dto.TicketDTO;
import com.blue.tnb.exception.PlayExceptions.InvalidDateException;
import com.blue.tnb.exception.PlayExceptions.PlayDeleteException;
import com.blue.tnb.exception.PlayExceptions.PlayNotFoundException;
import com.blue.tnb.exception.PlayExceptions.PlayUpdateException;
import com.blue.tnb.mapper.PlayMapperImpl;
import com.blue.tnb.mapper.TicketMapperImpl;
import com.blue.tnb.model.Play;
import com.blue.tnb.repository.PlayRepository;
import com.blue.tnb.repository.TicketRepository;
import com.blue.tnb.validator.PlayValidator;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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

    @Autowired
    private PlayValidator playValidator;

    @Autowired
    private TicketServiceImpl ticketServiceImpl;

    @Autowired
    private HazelcastInstance hazelcastInstance;

    @Override
    public List<PlayDTO> getAllPlays() {
        List<PlayDTO> plays=playRepository.findAll().stream()
                .map(playMapperImpl::convertPlayToPlayDTO)
                .collect(Collectors.toList());
        for(PlayDTO playDTO: plays){
            playDTO.setAvailableTicketsNumber(ticketRepository.countAllAvailableByPlayId(playDTO.getId()));
            playDTO.setBookedTicketsNumber(ticketRepository.countAllBookedTicketsByPlayId(playDTO.getId()));
        }
        return plays;
    }

    public PlayDTO getPlayById(Long id) throws PlayNotFoundException {
        return playRepository.findById(id)
                            .map(playMapperImpl::convertPlayToPlayDTO)
                            .orElseThrow(PlayNotFoundException::new);
    }

    public PlayDTO getPlayByName(String playName) throws PlayNotFoundException {
        return playRepository.findByPlayName(playName)
                            .map(playMapperImpl::convertPlayToPlayDTO)
                            .orElseThrow(PlayNotFoundException::new);
    }

    public Play addPlay(PlayDTO playDTO) throws InvalidDateException {
        if(playValidator.validateDateTime(playDTO.getPlayDate()) && playValidator.validateDateTime(playDTO.getAvailableDate())) {

            Play play = playRepository.saveAndFlush(this.playMapperImpl.convertPlayDTOToPlay(playDTO));

            TicketDTO ticketDTO;
            for (int i = 0; i < play.getTicketsNumber(); ++i) {
                ticketDTO = new TicketDTO();
                ticketDTO.setPlayId(play.getId());
                ticketDTO.setStatus(Status.FREE);
                try {
                    ticketServiceImpl.addTicket(ticketDTO);
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }
            }
            Map<Long, List<Long>> availableTickets=hazelcastInstance.getMap("tickets");
            List<Long> availableTicketsId=play.getTicketList().stream().map(ticket -> ticket.getId())
                                                                .collect(Collectors.toList());
            availableTickets.put(play.getId(),availableTicketsId);
            play = playRepository.getOne(play.getId()); //ticketList????
            return play;

        }
        else {throw new InvalidDateException();}
    }

    public PlayDTO updatePlay(PlayDTO playDTO, Long id) throws PlayUpdateException {
        if (playValidator.validateIdForUpdate(id)){
            Play existingPlay = playRepository.getOne(id);
            Play updatedPlay = this.playMapperImpl.convertPlayDTOToPlay(playDTO);

            existingPlay.setPlayName(updatedPlay.getPlayName());
            existingPlay.setPlayDate(updatedPlay.getPlayDate());
            existingPlay.setAvailableDate(updatedPlay.getAvailableDate());
            existingPlay.setRegisteredDate(existingPlay.getRegisteredDate());
            existingPlay.setLink(updatedPlay.getLink());
            existingPlay.setTicketsNumber(updatedPlay.getTicketsNumber()); //implement case when ticketsNumber changes
            return playMapperImpl.convertPlayToPlayDTO(playRepository.saveAndFlush(existingPlay));
        }
        else {throw new PlayUpdateException();}
    }

    public PlayDTO deletePlay(Long id) throws PlayDeleteException {
        if (playValidator.validateIdForDelete(id)) {
            Play play = playRepository.getOne(id);
            playRepository.deleteById(id);
            return playMapperImpl.convertPlayToPlayDTO(play);
        } else {
            throw new PlayDeleteException();
        }
    }

}
