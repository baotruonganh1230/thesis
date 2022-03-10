package com.example.thesis.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InsuranceInputParams {
    private Long id;
    private InsuranceCommon insuranceCommon;
    private Long cityId;
    private Long kcbId;
}
