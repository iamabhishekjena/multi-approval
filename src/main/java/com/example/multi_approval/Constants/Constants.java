package com.example.multi_approval.Constants;

public enum Constants {
    PREFIX_USER("USER"),
    LOGGED_IN("Y"),
    LOGGED_OUT("N");


    private final String value;

     Constants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
