
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

import com.blue.tnb.validator.DateValidator;
import com.blue.tnb.validator.PlayValidator;


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
    private DateValidator dateValidator;

    @Override
    public List<PlayDTO> getAllPlays() {
        List<PlayDTO> plays=playRepository.findAll().stream()
                .map(playMapperImpl::convertPlayToPlayDTO)
                .collect(Collectors.toList());
        for(PlayDTO playDTO: plays){
            playDTO.setAvailableTicketsNumber(ticketRepository.countAllAvailableByPlayId(playDTO.getId()));
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
        if(dateValidator.validateDate(playDTO.getAvailableDate()) && dateValidator.validateDate(playDTO.getPlayDate())) {
            Play play = playRepository.saveAndFlush(this.playMapperImpl.convertPlayDTOToPlay(playDTO));
            TicketDTO ticketDTO;
            for (int i = 0; i < play.getTicketsNumber(); ++i) {
                ticketDTO = new TicketDTO();
                ticketDTO.setPlayId(play.getId());
                ticketDTO.setStatus(Status.FREE);
                ticketRepository.saveAndFlush(this.ticketMapperImpl.ticketDTOToTicket(ticketDTO));
            }
            play = playRepository.getOne(play.getId()); //ticketList????
            return play;
        }
        else {throw new InvalidDateException();}
    }

    public Play updatePlay(PlayDTO playDTO, Long id) throws PlayUpdateException {
        if (playValidator.validateIdForUpdate(id)){
            Play existingPlay = playRepository.getOne(id);
            Play updatedPlay = this.playMapperImpl.convertPlayDTOToPlay(playDTO);
            existingPlay.setPlayName(updatedPlay.getPlayName());
            existingPlay.setPlayDate(updatedPlay.getPlayDate());
            existingPlay.setAvailableDate(updatedPlay.getAvailableDate());
            existingPlay.setRegisteredDate(existingPlay.getRegisteredDate());
            existingPlay.setLink(updatedPlay.getLink());
            existingPlay.setTicketsNumber(updatedPlay.getTicketsNumber()); //implement case when ticketsNumber changes
            return playRepository.saveAndFlush(existingPlay);
        }
        else {throw new PlayUpdateException();}

    }

    public Play deletePlay(Long id) throws PlayDeleteException {
        if (playValidator.validateIdForDelete(id)) {
            Play play = playRepository.getOne(id);
            playRepository.deleteById(id);
            return play;
        } else {
            throw new PlayDeleteException();
        }
    }

}
