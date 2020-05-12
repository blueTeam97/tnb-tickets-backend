//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.blue.tnb.validator;

import com.blue.tnb.model.Play;
import com.blue.tnb.repository.PlayRepository;
import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    public boolean validateDateTime(String localDateTimeAsString) {
        return GenericValidator.isDate(localDateTimeAsString, "yyyy-MM-dd HH:mm:ss", true);
    }

}
