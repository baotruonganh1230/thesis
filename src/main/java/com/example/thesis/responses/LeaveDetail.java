package com.example.thesis.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LeaveDetail {
    private Long id;

    private String employeeName;

    private String departmentName;

    private String applicationDate;

    private String fromDate;

    private String toDate;

    private Integer total;

    private Integer status;

    public LeaveDetail(Long id, String employeeName, String departmentName, LocalDate applicationDate, LocalDate fromDate, LocalDate toDate, Integer total, Integer status) {
        this.id = id;
        this.employeeName = employeeName;
        this.departmentName = departmentName;
        this.applicationDate = applicationDate.toString() + "T00:00:00.000Z";
        this.fromDate = fromDate.toString() + "T00:00:00.000Z";
        this.toDate = toDate.toString() + "T00:00:00.000Z";
        this.total = total;
        this.status = status;
    }
}
