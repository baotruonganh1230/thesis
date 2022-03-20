package com.example.thesis.requests;

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
public class InsuranceCommon {
    private Long id;

    private LocalDate from_date;

    private LocalDate to_date;

    private LocalDate issue_date;

    private String number;

    public void setFrom_date(String from_dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        //convert String to LocalDate
        this.from_date = LocalDate.parse(from_dateString, formatter);
    }

    public void setTo_date(String to_dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        //convert String to LocalDate
        this.to_date = LocalDate.parse(to_dateString, formatter);
    }

    public void setIssue_date(String issue_dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        //convert String to LocalDate
        this.issue_date = LocalDate.parse(issue_dateString, formatter);
    }
}
