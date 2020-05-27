//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.blue.tnb.validator;

import com.blue.tnb.dto.PlayDTO;
import com.blue.tnb.model.Play;
import com.blue.tnb.repository.PlayRepository;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public boolean validateDateTime(String playDateAsString, String availableDateAsString) {
        boolean ok=GenericValidator.isDate(playDateAsString, "yyyy-MM-dd HH:mm:ss", true)
                && GenericValidator.isDate(availableDateAsString, "yyyy-MM-dd HH:mm:ss", true);
        if(!ok)
            ok=GenericValidator.isDate(playDateAsString, "yyyy-MM-dd HH:mm", true)
                    && GenericValidator.isDate(playDateAsString, "yyyy-MM-dd HH:mm", true);
        if(ok) {
            LocalDateTime playDateAslocalDateTime = convertStringToLocalDateTime(playDateAsString);
            LocalDateTime availableDateAsLocalDateTime = convertStringToLocalDateTime(availableDateAsString);

            if(playDateAslocalDateTime.isAfter(availableDateAsLocalDateTime)) {
                if(playDateAslocalDateTime.isAfter(LocalDateTime.now().minusMinutes(2))) {
                    return true;
                }
            }
        }
        return false;
    }

    public LocalDateTime convertStringToLocalDateTime(String dateAsString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime=null;
        try{
             dateTime= LocalDateTime.parse(dateAsString, formatter);
        }
        catch (Exception ex){
            DateTimeFormatter formatter2=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            dateTime=LocalDateTime.parse(dateAsString,formatter2);
        }
        return dateTime;
    }

    public boolean validateImageUrl(String imageUrl) {
        return new UrlValidator().isValid(imageUrl);
    }


}
