//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.blue.tnb.validator;

import com.blue.tnb.dto.PlayDTO;
import com.blue.tnb.repository.PlayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlayValidator {
    @Autowired
    private PlayRepository playRepository;
    @Autowired
    private DateValidator dateValidator;

    public PlayValidator() {
    }

    public boolean validateId(Long id) {
        if (id == null) {
            return false;
        } else if (id <= 0L) {
            return false;
        } else {
            return this.playRepository.findById(id).isPresent();
        }
    }

    public boolean validateTicketsNumber(Integer ticketsNumber) {
        if (ticketsNumber == null) {
            return false;
        } else {
            return ticketsNumber > 0;
        }
    }

    public boolean validateUpdate(PlayDTO playDTO) {
        if (!this.validateId(playDTO.getId())) {
            return false;
        } else if (!this.dateValidator.validateDate(playDTO.getPlayDate())) {
            return false;
        } else if (!this.dateValidator.validateDate(playDTO.getAvailableDate())) {
            return false;
        } else if (!this.dateValidator.validateDate(playDTO.getRegisteredDate())) {
            return false;
        } else {
            return this.validateTicketsNumber(playDTO.getTicketsNumber());
        }
    }
}
