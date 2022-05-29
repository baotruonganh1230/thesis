package com.example.thesis.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventRequest {
    private Long id;
    private String notes;
    private String title;
    private LocalDateTime time;
    private LocalDateTime timeEnd;
    private String type;
    private String senderName;
    private String location;

    public void setTime(String timeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        //convert String to LocalDate
        this.time = LocalDateTime.parse(timeString, formatter);
    }

    public void setTimeEnd(String timeEndString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        //convert String to LocalDate
        this.timeEnd = LocalDateTime.parse(timeEndString, formatter);
    }
}
