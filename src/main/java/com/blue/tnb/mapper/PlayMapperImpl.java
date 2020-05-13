package com.blue.tnb.mapper;

import com.blue.tnb.dto.PlayDTO;
import com.blue.tnb.model.Play;
import org.springframework.stereotype.Component;

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
            Play play = new Play(playDTO);
            return play;
        }
    }

    @Override
    public PlayDTO convertPlayToPlayDTO(Play play) {
        if (play == null) {
            return null;
        } else {
            PlayDTO playDTO = new PlayDTO(play);
            return playDTO;
        }
    }

    @Override
    public List<Play> convertPlayDTOToPlayList(List<PlayDTO> playDTOList) {
        if (playDTOList == null) {
            return null;
        } else {
            List<Play> list = new ArrayList();
            Iterator var3 = playDTOList.iterator();

            while(var3.hasNext()) {
                PlayDTO playDTO = (PlayDTO)var3.next();
                list.add(this.convertPlayDTOToPlay(playDTO));
            }
            return list;
        }
    }

    @Override
    public List<PlayDTO> convertPlayToPlayDTOList(List<Play> playList) {
        if (playList == null) {
            return null;
        } else {
            List<PlayDTO> list = new ArrayList();
            Iterator var3 = playList.iterator();

            while(var3.hasNext()) {
                Play play = (Play)var3.next();
                list.add(this.convertPlayToPlayDTO(play));
            }
            return list;
        }
    }


}
