
package com.blue.tnb.service;

import com.blue.tnb.constants.Status;
import com.blue.tnb.dto.PlayDTO;
import com.blue.tnb.dto.TicketDTO;
import com.blue.tnb.exception.PlayExceptions.InvalidDateException;
import com.blue.tnb.exception.PlayExceptions.PlayDeleteException;
import com.blue.tnb.exception.PlayExceptions.PlayNotFoundException;
import com.blue.tnb.exception.PlayExceptions.PlayUpdateException;
import com.blue.tnb.exception.TicketExceptions.TicketsNumberException;
import com.blue.tnb.mapper.PlayMapperImpl;
import com.blue.tnb.mapper.TicketMapperImpl;
import com.blue.tnb.model.Play;
import com.blue.tnb.repository.PlayRepository;
import com.blue.tnb.repository.TicketRepository;
import com.blue.tnb.validator.PlayValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private PlayValidator playValidator;

    @Autowired
    private TicketMapperImpl ticketMapperImpl;

    @Override
    public List<PlayDTO> getAllPlays() {
        List<PlayDTO> plays = playRepository.findAll().stream()
                .map(playMapperImpl::convertPlayToPlayDTO)
                .collect(Collectors.toList());
        for (PlayDTO playDTO : plays) {
            playDTO.setAvailableTicketsNumber(ticketRepository.countAllAvailableByPlayId(playDTO.getId()));
            playDTO.setBookedTicketsNumber(ticketRepository.countAllBookedTicketsByPlayId(playDTO.getId()));
        }
        return plays;
    }

    public PlayDTO getPlayById(Long id) throws PlayNotFoundException {
        PlayDTO playDTO = playRepository.findById(id)
                .map(playMapperImpl::convertPlayToPlayDTO)
                .orElseThrow(PlayNotFoundException::new);
        playDTO.setAvailableTicketsNumber(ticketRepository.countAllAvailableByPlayId(playDTO.getId()));
        return playDTO;
    }

    public PlayDTO getPlayByName(String playName) throws PlayNotFoundException {
        return playRepository.findByPlayName(playName)
                .map(playMapperImpl::convertPlayToPlayDTO)
                .orElseThrow(PlayNotFoundException::new);
    }

    public PlayDTO addPlay(PlayDTO playDTO) throws InvalidDateException {

        if (!checkDateTimeFormat(playDTO)) {
            throw new InvalidDateException();
        }
        else {
            Play play = playRepository.save(this.playMapperImpl.convertPlayDTOToPlay(playDTO));
            populateTicketsListPlay(play);
            return playMapperImpl.convertPlayToPlayDTO(playRepository.save(play));
        }
    }

    public PlayDTO updatePlay(PlayDTO playDTO, Long id) throws PlayUpdateException, InvalidDateException, TicketsNumberException {
        if (!playValidator.validateIdForUpdate(id)) {
            throw new PlayUpdateException();
        } else if (!playValidator.validateDateTime(playDTO.getAvailableDate())
                && !playValidator.validateDateTime(playDTO.getPlayDate())) {
            throw new InvalidDateException();
        } else {
            Play existingPlay = playRepository.getOne(id);
            Play updatedPlay = this.playMapperImpl.convertPlayDTOToPlay(playDTO);
            existingPlay.setPlayName(updatedPlay.getPlayName());
            existingPlay.setPlayDate(updatedPlay.getPlayDate());
            existingPlay.setAvailableDate(updatedPlay.getAvailableDate());
            existingPlay.setLink(updatedPlay.getLink());
            if (existingPlay.getTicketsNumber() > updatedPlay.getTicketsNumber()) {
                throw new TicketsNumberException();
            } else if (existingPlay.getTicketsNumber() < updatedPlay.getTicketsNumber()) {
                updateTicketsListPlay(existingPlay, updatedPlay.getTicketsNumber());
            }

            existingPlay.setTicketsNumber(updatedPlay.getTicketsNumber());

            return playMapperImpl.convertPlayToPlayDTO(playRepository.saveAndFlush(existingPlay));
        }
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

    public void populateTicketsListPlay(Play play) {

        TicketDTO ticketDTO;
        play.setTicketList(new ArrayList<>());
        for (int i = 0; i < play.getTicketsNumber(); i++) {
            ticketDTO = new TicketDTO();
            ticketDTO.setPlayId(play.getId());
            ticketDTO.setStatus(Status.FREE);
            play.getTicketList().add(ticketMapperImpl.ticketDTOToTicket(ticketDTO));
        }
    }

    public void updateTicketsListPlay(Play existingPlay, int updatedNumberOfTickets) {
        TicketDTO ticketDTO;
        for (int i = 0; i < updatedNumberOfTickets - existingPlay.getTicketsNumber(); i++) {
            ticketDTO = new TicketDTO();
            ticketDTO.setPlayId(existingPlay.getId());
            ticketDTO.setStatus(Status.FREE);
            existingPlay.getTicketList().add(ticketMapperImpl.ticketDTOToTicket(ticketDTO));
        }
    }

    public boolean checkDateTimeFormat(PlayDTO playDTO) {
        playDTO.setPlayDate(playValidator.checkDateTimeFormat(playDTO.getPlayDate()));
        playDTO.setAvailableDate(playValidator.checkDateTimeFormat(playDTO.getAvailableDate()));
        if (playValidator.validateDateTime(playDTO.getPlayDate()) && playValidator.validateDateTime(playDTO.getAvailableDate())) {
            return true;
        } else {
            return false;
        }
    }
}