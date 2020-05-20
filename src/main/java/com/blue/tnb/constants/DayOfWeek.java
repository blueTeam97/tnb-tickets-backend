package com.blue.tnb.constants;

public enum DayOfWeek {
    MONDAY(1),
    TUESDAY(2),
    WEDNESADY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6),
    SUNDAY(7);

    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    DayOfWeek(int value) {
        this.value = value;
    }
}
