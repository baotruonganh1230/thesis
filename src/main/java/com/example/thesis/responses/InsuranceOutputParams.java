package com.example.thesis.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InsuranceOutputParams {
    private InsuranceCommonResponse social;
    private InsuranceCommonResponse unemployment;
    private InsuranceCommonResponse health;
    private Long cityId;
    private Long kcbId;
}
