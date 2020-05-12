package com.blue.tnb.constants;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class StatusConverter implements AttributeConverter<Status,String> {
    @Override
    public String convertToDatabaseColumn(Status status) {
        return status.getValue();
    }

    @Override
    public Status convertToEntityAttribute(String s) {
        switch(s){
            case "free":
                return Status.FREE;
            case "booked":
                return Status.BOOKED;
            case "pickedup":
                return Status.PICKEDUP;
        }
        return null;
    }
}
