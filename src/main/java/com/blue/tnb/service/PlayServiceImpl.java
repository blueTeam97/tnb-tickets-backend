
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
import com.blue.tnb.model.Ticket;
import com.blue.tnb.repository.PlayRepository;
import com.blue.tnb.repository.TicketRepository;
import com.blue.tnb.repository.UserRepository;
import com.blue.tnb.validator.PlayValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
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

    @Autowired
    private UserRepository userRepository;

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

    public List<PlayDTO> getAllPlaysForUser(String userCredential) {
        List<PlayDTO> plays = playRepository.getAllAvailablePlays().stream()
                .map(playMapperImpl::convertPlayToPlayDTO)
                .collect(Collectors.toList());
        List<PlayDTO> availablePlays=new ArrayList<>();
        for (PlayDTO playDTO : plays) {
            String[] headerSplitted=userCredential.substring("Bearer".length()).trim().split("\\.");
            byte[] userDecoded= Base64.getDecoder().decode(headerSplitted[1]);
            String userCredentialDecoded=new String(userDecoded);
            String userEmail=userCredentialDecoded.split(",")[0].split(":")[1];
            userEmail=userEmail.substring(1,userEmail.length()-1);

            Long userId=userRepository.getUserIdByEmail(userEmail);

            Optional<Ticket> lastBookedTicket=ticketRepository.findAllByUserId(userId).stream()
                    .filter(ticket->ticket.getStatus().equals(Status.BOOKED))
                    .max((t1,t2)->t1.getBookDate().compareTo(t2.getBookDate()));
            if(lastBookedTicket.isPresent() && lastBookedTicket.get().getBookDate()
                    .until(playValidator.convertStringToLocalDateTime(playDTO.getAvailableDate()),ChronoUnit.DAYS)>30){
                playDTO.setAvailableTicketsNumber(ticketRepository.countAllAvailableByPlayId(playDTO.getId()));
                playDTO.setBookedTicketsNumber(ticketRepository.countAllBookedTicketsByPlayId(playDTO.getId()));
                availablePlays.add(playDTO);
            }
            else if(!lastBookedTicket.isPresent()){
                playDTO.setAvailableTicketsNumber(ticketRepository.countAllAvailableByPlayId(playDTO.getId()));
                playDTO.setBookedTicketsNumber(ticketRepository.countAllBookedTicketsByPlayId(playDTO.getId()));
                availablePlays.add(playDTO);
            }
        }
        return availablePlays;
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
        } else if (! playValidator.validateDateTime(playDTO.getPlayDate(), playDTO.getAvailableDate())) {
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

    public List<Play> getNextAvailablePlays(LocalDate localDateFrom, LocalDate localDateTo) {
        return playRepository.getNextAvailablePlays(localDateFrom, localDateTo);
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
        if (playValidator.validateDateTime(playDTO.getPlayDate(), playDTO.getAvailableDate())) {
            return true;
        } else {
            return false;
        }
    }


}
