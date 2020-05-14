//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.blue.tnb.validator;

import com.blue.tnb.dto.PlayDTO;
import com.blue.tnb.model.Play;
import com.blue.tnb.repository.PlayRepository;
import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Component
public class PlayValidator {

    @Autowired
    private PlayRepository playRepository;

    public PlayValidator() {}

    public boolean validateIdForDelete(Long id) {
        if(id == null) {
            return false;
        }
        else if(id <= 0) {
            return false;
        }
        Optional<Play> existingPlay = playRepository.findById(id);
        if(existingPlay.isPresent()) {
            return true;
        }
        else {return false;}
    }

    public boolean validateIdForUpdate(Long id) {
        return validateIdForDelete(id);
    }

    public String checkDateTimeFormat(String localDateTimeAsString) {
        String[] parts = localDateTimeAsString.split("-");
        StringBuilder str = new StringBuilder(parts[1]);
        StringBuilder localDateTime = new StringBuilder(localDateTimeAsString);
        String secondStr;
        if(parts[1].length() != 2) {
            str.insert(0, "0");
            localDateTime.replace(5,6, str.toString());
        }

        secondStr = localDateTime.toString();

        if(localDateTime.toString().contains("T")) {
            secondStr = localDateTime.toString().replace("T", " ");
        }

        if(localDateTimeAsString.equals(secondStr)) {
            return localDateTimeAsString;
        }
        else {return secondStr;}

    }

    public boolean validateDateTime(String localDateTimeAsString) {
        return GenericValidator.isDate(localDateTimeAsString, "yyyy-MM-dd HH:mm:ss", true);
    }

}
