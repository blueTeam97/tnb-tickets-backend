package com.blue.tnb.constants;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {

    private static String convertDateToString(Date date){
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString=formatter.format(date);
        if(dateString.contains("T")){
            dateString.replace("T"," ");
        }
        return dateString;
    }

    public static LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
    private static LocalDateTime convertToLocalDateTimeWithoutSeconds(String stringDate){
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime convertedDate=null;
        try{
            return LocalDateTime.parse(stringDate,formatter);
        }
        catch (Exception ex){
            return null;
        }
    }
    private static LocalDateTime convertToLocalDateTimeWithSeconds(String stringDate){
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime convertedDate=null;
        try{
            return LocalDateTime.parse(stringDate,formatter);
        }
        catch (Exception ex){
            return null;
        }
    }

    public static LocalDateTime convertStringToLocalDateTime(String stringDate){
        LocalDateTime aux=convertToLocalDateTimeWithSeconds(stringDate);
        return aux!=null?aux:convertToLocalDateTimeWithoutSeconds(stringDate);
    }
}
