package com.example.thesis.entities;

public enum AccountStatus {
    ENABLE("enable"), DISABLE("disable");

    AccountStatus(String value) {
        this.value = value;
    }

    private final String value;

    public String getValue() {
        return value;
    }

    public static AccountStatus getByValue(String value){
        return AccountStatus.valueOf(value.toUpperCase());
    }

    @Override
    public String toString() {
        return this.value.toUpperCase();
    }
}