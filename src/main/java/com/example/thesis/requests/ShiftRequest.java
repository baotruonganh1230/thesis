package com.example.thesis.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShiftRequest {
    private String name;

    private LocalTime from;

    private LocalTime to;

    public void setFrom(String timeInString) {
        this.from = LocalTime.of(Integer.parseInt(timeInString.substring(11,13)),
                Integer.parseInt(timeInString.substring(14,16)),
                Integer.parseInt(timeInString.substring(17,19)),
                Integer.parseInt(timeInString.substring(20,23)) * (int) Math.pow(10, 6));
    }

    public void setTo(String timeOutString) {
        this.to = LocalTime.of(Integer.parseInt(timeOutString.substring(11,13)),
                Integer.parseInt(timeOutString.substring(14,16)),
                Integer.parseInt(timeOutString.substring(17,19)),
                Integer.parseInt(timeOutString.substring(20,23)) * (int) Math.pow(10, 6));

    }
}
