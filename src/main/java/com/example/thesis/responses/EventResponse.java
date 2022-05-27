package com.example.thesis.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class EventResponse {
    private Long id;
    private String notes;
    private String title;
    private String time;
    private String timeEnd;
    private String type;
    private String location;

    public EventResponse(Long id, String notes, String title, LocalDateTime time, LocalDateTime timeEnd, String type, String location) {
        this.id = id;
        this.notes = notes;
        this.title = title;
        this.time = time.toString() + "Z";
        this.timeEnd = timeEnd.toString() + "Z";
        this.type = type;
        this.location = location;
    }
}
