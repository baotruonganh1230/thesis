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

    private LocalDate applicationDate;

    private LocalDate fromDate;

    private LocalDate toDate;

    private Integer total;

    private Integer status;
}
