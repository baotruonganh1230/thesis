package com.example.thesis.responses;

import com.example.thesis.requests.InsuranceInputParams;
import com.example.thesis.requests.JobDetailInputParams;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmployeeResponse {
    private Long id;

    private PersonalDetailOutputParams personalDetail;

    private JobDetailInputParams jobDetail;

    private InsuranceInputParams insuranceDetail;
}
