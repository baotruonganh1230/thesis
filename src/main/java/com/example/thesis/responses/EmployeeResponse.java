package com.example.thesis.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class EmployeeResponse {
    private Long id;

    private String avatar;

    private String firstName;

    private String lastName;

    private String email;

    private String permanentAddress;

    private String temporaryAddress;

    private String phone;

    private String departmentName;

    private String jobTitle;

    private LocalDate dateOfBirth;

    private String pit;

    private Integer salaryGroup;

    private BigDecimal gross_salary;

    private List<Bonus> bonus_lists;

}
