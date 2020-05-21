package com.blue.tnb.mapper;

import com.blue.tnb.dto.PlayDTO;
import com.blue.tnb.model.Play;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class PlayMapperImpl implements PlayMapper {

    public PlayMapperImpl() {}

    @Override
    public Play convertPlayDTOToPlay(PlayDTO playDTO) {
        if (playDTO == null) {
            return null;
        } else {
            Play play = new Play();
            play.setPlayName(playDTO.getPlayName());
            play.setAvailableDate(convertStringToLocalDateTime(playDTO.getAvailableDate()));
            play.setPlayDate(convertStringToLocalDateTime(playDTO.getPlayDate()));
            play.setLink(playDTO.getLink());
            play.setTicketsNumber(playDTO.getTicketsNumber());
            play.setImageUrl(playDTO.getImageUrl());
            return play;
        }
    }

    @Override
    public PlayDTO convertPlayToPlayDTO(Play play) {
        if (play == null) {
            return null;
        } else {
            PlayDTO playDTO = new PlayDTO();
            playDTO.setId(play.getId());
            playDTO.setPlayName(play.getPlayName());
            playDTO.setAvailableDate(play.getAvailableDate().toString().replace("T", " "));
            playDTO.setPlayDate(play.getPlayDate().toString().replace("T", " "));
            playDTO.setRegisteredDate(play.getRegisteredDate().toString().replace("T", " "));
            playDTO.setLink(play.getLink());
            playDTO.setTicketsNumber(play.getTicketsNumber());
            playDTO.setImageUrl(play.getImageUrl());
            return playDTO;
        }
    }

    public LocalDateTime convertStringToLocalDateTime(String dateAsString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dateAsString, formatter);
        return dateTime;
    }


}
