package com.example.thesis.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckinRequest {
    private Long userId;
    private String deviceId;
    private LocalDate date;
    private LocalTime timeIn;
    private LocalTime timeOut;

    public void setDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        //convert String to LocalDate
        this.date = LocalDate.parse(dateString, formatter);
    }
}
