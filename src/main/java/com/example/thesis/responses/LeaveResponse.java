package com.example.thesis.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LeaveResponse {
    private Long leaveType;
    private Integer amount;
    private LocalDate fromDate;
    private LocalDate toDate;
    private Long userId;
    private String reason;
    private Integer status;

    public void setFromDate(String fromDateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        //convert String to LocalDate
        this.fromDate = LocalDate.parse(fromDateString, formatter);
    }

    public void setToDate(String toDateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        //convert String to LocalDate
        this.toDate = LocalDate.parse(toDateString, formatter);
    }
}
