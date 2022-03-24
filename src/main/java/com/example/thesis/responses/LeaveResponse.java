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
    private String applicationDate;
    private String fromDate;
    private String toDate;
    private Long userId;
    private String reason;
    private Integer status;

    public LeaveResponse(Long leaveType, Integer amount, LocalDate applicationDate,LocalDate fromDate, LocalDate toDate, Long userId, String reason, Integer status) {
        this.leaveType = leaveType;
        this.amount = amount;
        this.applicationDate = applicationDate.toString() + "T00:00:00.000Z";
        this.fromDate = fromDate.toString() + "T00:00:00.000Z";
        this.toDate = toDate.toString() + "T00:00:00.000Z";
        this.userId = userId;
        this.reason = reason;
        this.status = status;
    }

    public void setFromDate(String fromDateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        //convert String to LocalDate
        this.fromDate = LocalDate.parse(fromDateString, formatter).toString();
    }

    public void setToDate(String toDateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        //convert String to LocalDate
        this.toDate = LocalDate.parse(toDateString, formatter).toString();
    }
}
