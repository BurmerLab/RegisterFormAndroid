package com.mytway.pojo;

public class Distance {

    private String text = "";
    private int value = 0;

    public Distance(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public Distance() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
