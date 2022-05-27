package com.example.thesis.requests;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class SendEventRequest {
    private Long userId;
    private String notes;
    private String title;
    private LocalDateTime time;
    private LocalDateTime timeEnd;
    private String type;
    private String location;
    private List<Long> eid;

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
