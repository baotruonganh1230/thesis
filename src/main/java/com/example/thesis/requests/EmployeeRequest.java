package com.example.thesis.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRequest {
    private JobDetailInputParams jobDetail;
    private PersonalDetailInputParams personalDetail;
    private AssignAccountInputParams accountDetail;
    private InsuranceInputParams insuranceDetail;
}
