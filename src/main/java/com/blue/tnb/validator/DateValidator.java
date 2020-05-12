package com.blue.tnb.validator;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.format.DateTimeFormatter;

@Component
public class DateValidator {

    public boolean validateDate(String date) {
        if(StringUtils.isEmpty(date))
            return false;
        if(date.contentEquals("T")){
            return false;
        }
        else {
            String[] dateSplitted=date.split(" ");
            char dateDelimiter=' ';
            char timeDelimiter=' ';
            for(char ch : dateSplitted[0].toCharArray()){
                if(ch < '0' || ch > '9'){
                    dateDelimiter = ch;
                    break;
                }
            }
            for(char ch:dateSplitted[1].toCharArray()){
                if(ch < '0' || ch > '9'){
                    timeDelimiter = ch;
                    break;
                }
            }
            StringBuilder format = new StringBuilder();
            format.append("yyyy").append(dateDelimiter).append("MM")
                    .append(dateDelimiter).append("dd");
            format.append(" ");
            format.append("hh").append(timeDelimiter)
                    .append("mm").append(timeDelimiter)
                    .append("ss");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format.toString());
            System.out.println(format.toString());
            try{
                formatter.parse(date);
            }
            catch (Exception ex){
                return false;
            }
            return true;
        }
    }
}
