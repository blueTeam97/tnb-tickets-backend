package com.blue.tnb.mapper;

import com.blue.tnb.dto.PlayDTO;
import com.blue.tnb.model.Play;

import java.util.List;


public interface PlayMapper {

    Play convertPlayDTOToPlay(PlayDTO playDTO);
    PlayDTO convertPlayToPlayDTO(Play play);

    //List<Play> convertPlayDTOToPlayList(List<PlayDTO> playDTOList);
    //List<PlayDTO> convertPlayToPlayDTOList(List<Play> playList);

}