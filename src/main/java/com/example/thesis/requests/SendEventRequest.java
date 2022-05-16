package com.example.thesis.requests;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class SendEventRequest {
    private Long userId;
    private String notes;
    private String title;
    private LocalDateTime time;

    public void setTime(String fromDateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        //convert String to LocalDate
        this.time = LocalDateTime.parse(fromDateString, formatter);
    }
}