package com.kalingantech.neverduedev.Home;

public class Recycler_new_sub_model {
    int sub_image;
    String sub_name;
    int colour;

    public Recycler_new_sub_model(int sub_image, String sub_name, int colour) {
        this.sub_image = sub_image;
        this.sub_name = sub_name;
        this.colour = colour;
    }

    public int getSub_image() {
        return sub_image;
    }

    public void setSub_image(int sub_image) {
        this.sub_image = sub_image;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public int getColour() {
        return colour;
    }

    public void setColour(int colour) {
        this.colour = colour;
    }
}
