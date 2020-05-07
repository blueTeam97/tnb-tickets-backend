package com.blue.tnb.service;

import com.blue.tnb.dto.PlayDTO;
import com.blue.tnb.exception.PlayNotFoundException;
import java.util.List;

public interface PlayService {

    List<PlayDTO> getAllPlays();
    PlayDTO getPlayByName(String playName) throws PlayNotFoundException;
    PlayDTO getPlayById(Long id) throws PlayNotFoundException;

    //List<Ticket> findAllTicketsForPlay(Long id);

}
