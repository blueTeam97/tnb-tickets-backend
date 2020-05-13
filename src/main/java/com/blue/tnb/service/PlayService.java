package com.blue.tnb.service;

import com.blue.tnb.dto.PlayDTO;
import com.blue.tnb.exception.PlayExceptions.InvalidDateException;
import com.blue.tnb.exception.PlayExceptions.PlayDeleteException;
import com.blue.tnb.exception.PlayExceptions.PlayNotFoundException;
import com.blue.tnb.exception.PlayExceptions.PlayUpdateException;
import com.blue.tnb.model.Play;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlayService {

    List<PlayDTO> getAllPlays();
    PlayDTO getPlayByName(String playName) throws PlayNotFoundException;
    PlayDTO getPlayById(Long id) throws PlayNotFoundException;

    PlayDTO addPlay(PlayDTO playDTO) throws InvalidDateException;
    PlayDTO updatePlay(PlayDTO playDTO, Long id) throws PlayUpdateException, InvalidDateException;
    PlayDTO deletePlay(Long id) throws PlayDeleteException;

}
