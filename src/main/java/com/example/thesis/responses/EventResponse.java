package com.example.thesis.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class EventResponse {
    private String notes;
    private String title;
    private String time;

    public EventResponse(String notes, String title, LocalDateTime time) {
        this.notes = notes;
        this.title = title;
        this.time = time.toString() + "Z";
    }
}
