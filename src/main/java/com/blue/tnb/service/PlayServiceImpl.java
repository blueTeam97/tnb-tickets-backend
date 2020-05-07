package com.blue.tnb.service;

import com.blue.tnb.dto.PlayDTO;
import com.blue.tnb.exception.PlayNotFoundException;
import com.blue.tnb.mapper.PlayMapper;
import com.blue.tnb.mapper.PlayMapperImpl;
import com.blue.tnb.model.Play;
import com.blue.tnb.repository.PlayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service("playService")
public class PlayServiceImpl implements PlayService {

    public PlayServiceImpl() {}

    @Autowired
    private PlayRepository playRepository;

    @Autowired
    private PlayMapperImpl playMapperImpl;


    @Override
    public List<PlayDTO> getAllPlays() {
        return playRepository.findAll().stream()
                                .map(this::toPlayDTO)
                                .collect(Collectors.toList());
    }

    @Override
    public PlayDTO getPlayByName(String playName) throws PlayNotFoundException {
        return playRepository.findAllByPlayName(playName)
                            .map(this::toPlayDTO)
                            .orElseThrow(PlayNotFoundException::new);

    }

    @Override
    public PlayDTO getPlayById(Long id) throws PlayNotFoundException {
        return playRepository.findAllById(id)
                .map(this::toPlayDTO)
                .orElseThrow(PlayNotFoundException::new);
    }

    private PlayDTO toPlayDTO(Play play){
        return playMapperImpl.convertPlayToPlayDTO(play);
    }

}
