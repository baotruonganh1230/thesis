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
public class InsuranceCommonResponse {
    private Long id;

    private String from_date;

    private String to_date;

    private String issue_date;

    private String number;

    public InsuranceCommonResponse(Long id, LocalDate from_date, LocalDate to_date, LocalDate issue_date, String number) {
        this.id = id;
        this.from_date = from_date.toString() + "'T'00:00:00.000Z";
        this.to_date = to_date.toString() + "'T'00:00:00.000Z";
        this.issue_date = issue_date.toString() + "'T'00:00:00.000Z";
        this.number = number;
    }

    public void setFrom_date(String from_dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        //convert String to LocalDate
        this.from_date = LocalDate.parse(from_dateString, formatter).toString();
    }

    public void setTo_date(String to_dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        //convert String to LocalDate
        this.to_date = LocalDate.parse(to_dateString, formatter).toString();
    }

    public void setIssue_date(String issue_dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        //convert String to LocalDate
        this.issue_date = LocalDate.parse(issue_dateString, formatter).toString();
    }
}
