package com.example.thesis.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckinResponse {
    private Long attendanceId;

    private Integer status;

    private String date;

    private LocalTime time_in;
    private LocalTime time_out;

    public CheckinResponse(Long attendanceId, Integer status, LocalDate date, LocalTime time_in, LocalTime time_out) {
        this.attendanceId = attendanceId;
        this.status = status;
        this.date = date.toString() + "T00:00:00.000Z";
        this.time_in = time_in;
        this.time_out = time_out;
    }
}
