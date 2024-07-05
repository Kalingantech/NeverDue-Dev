package com.kalingantech.neverduedev.New_subscription;

public class Currency_logo_model {
    String curr_logo;
    String curr_code;
    String curr_name;

    public Currency_logo_model(String curr_logo, String curr_code, String curr_name) {
        this.curr_logo = curr_logo;
        this.curr_code = curr_code;
        this.curr_name = curr_name;
    }

    public String getCurr_logo() {
        return curr_logo;
    }

    public void setCurr_logo(String curr_logo) {
        this.curr_logo = curr_logo;
    }

    public String getCurr_code() {
        return curr_code;
    }

    public void setCurr_code(String curr_code) {
        this.curr_code = curr_code;
    }

    public String getCurr_name() {
        return curr_name;
    }

    public void setCurr_name(String curr_name) {
        this.curr_name = curr_name;
    }
}






