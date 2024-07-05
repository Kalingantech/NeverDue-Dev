package com.kalingantech.neverduedev.New_subscription;

public class Reminder_model {
    int days;
    String string_days;

    public Reminder_model(int days, String string_days) {
        this.days = days;
        this.string_days = string_days;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public String getString_days() {
        return string_days;
    }

    public void setString_days(String string_days) {
        this.string_days = string_days;
    }
}
