package com.blue.tnb.constants;

public enum Status {
    FREE("free"),
    BOOKED("booked"),
    PICKEDUP("pickedup");
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    Status(String value){
        this.value=value;
    }
}
