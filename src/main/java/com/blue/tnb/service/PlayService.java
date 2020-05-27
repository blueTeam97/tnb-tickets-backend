package com.blue.tnb.service;

import com.blue.tnb.dto.PlayDTO;
import com.blue.tnb.dto.UserPlaysPopulator;
import com.blue.tnb.exception.PlayExceptions.*;
import com.blue.tnb.exception.TicketExceptions.TicketsNumberException;
import com.blue.tnb.model.Play;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface PlayService {

    List<PlayDTO> getAllPlays();
    PlayDTO getPlayByName(String playName) throws PlayNotFoundException;
    PlayDTO getPlayById(Long id) throws PlayNotFoundException;

    PlayDTO addPlay(PlayDTO playDTO) throws InvalidDateException, InvalidImageUrlException;
    PlayDTO updatePlay(PlayDTO playDTO, Long id) throws PlayUpdateException, InvalidDateException, TicketsNumberException, InvalidImageUrlException;
    PlayDTO deletePlay(Long id) throws PlayDeleteException;

    List<PlayDTO> getAllPlaysForUser(String userCredential);

    UserPlaysPopulator populateUserWithPlays(String userCredential);
    List<PlayDTO> getAllBookedPlaysForLoggedUser(String userCredential);
}
