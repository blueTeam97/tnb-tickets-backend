package com.blue.tnb.service;

import com.blue.tnb.dto.PlayDTO;
import com.blue.tnb.exception.PlayNotFoundException;
import com.blue.tnb.mapper.PlayMapperImpl;
import com.blue.tnb.model.Play;
import com.blue.tnb.repository.PlayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
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
                                .map(playMapperImpl::convertPlayToPlayDTO)
                                .collect(Collectors.toList());
    }

    @Override
    public PlayDTO getPlayByName(String playName) throws PlayNotFoundException {
        return playRepository.findAllByPlayName(playName)
                            .map(playMapperImpl::convertPlayToPlayDTO)
                            .orElseThrow(PlayNotFoundException::new);

    }

    @Override
    public PlayDTO getPlayById(Long id) throws PlayNotFoundException {
        return playRepository.findAllById(id)
                .map(playMapperImpl::convertPlayToPlayDTO)
                .orElseThrow(PlayNotFoundException::new);
    }

    @Override
    public Play addPlay(PlayDTO playDTO) {
        return playRepository.saveAndFlush(playMapperImpl.convertPlayDTOToPlay(playDTO));
    }

    @Override
    public Play updatePlay(PlayDTO playDTO, Long id) {
        Play existingPlay = playRepository.getOne(id);
        Play play = playMapperImpl.convertPlayDTOToPlay(playDTO);
        existingPlay.setPlayName(play.getPlayName());
        existingPlay.setPlayDate(play.getPlayDate());
        existingPlay.setAvailableDate(play.getAvailableDate());
        existingPlay.setRegisteredDate(play.getRegisteredDate());
        existingPlay.setLink(play.getLink());
        existingPlay.setTicketsNumber(play.getTicketsNumber());
        return playRepository.saveAndFlush(existingPlay);
    }

    @Override
    public Play deletePlay(Long id) {
        Optional<Play> existingPlay = playRepository.findById(id);
        existingPlay.ifPresent(play -> playRepository.delete(play));
        return existingPlay.orElse(new Play());
    }



}