package com.example.thesis.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmployeeResponse {
    private Long id;

    private PersonalDetailOutputParams personalDetail;

    private JobDetailOutputParams jobDetail;

    private InsuranceOutputParams insuranceDetail;
}
