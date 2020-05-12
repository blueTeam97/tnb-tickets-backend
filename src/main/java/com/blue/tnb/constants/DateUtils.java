package com.blue.tnb.constants;

import java.text.SimpleDateFormat;
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
}
