package com.blue.tnb.service;

import com.blue.tnb.dto.PlayDTO;
import com.blue.tnb.dto.TicketDTO;
import com.blue.tnb.exception.PlayNotFoundException;
import com.blue.tnb.model.Play;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlayService {

    List<PlayDTO> getAllPlays();
    PlayDTO getPlayByName(String playName) throws PlayNotFoundException;
    PlayDTO getPlayById(Long id) throws PlayNotFoundException;

    Play addPlay(PlayDTO playDTO);
    Play updatePlay(PlayDTO playDTO, Long id);
    Play deletePlay(Long id);
}
