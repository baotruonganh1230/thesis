package com.example.thesis.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private BigDecimal basicSalary;

    private MonthlyInfo monthlyInfo;

    private BigDecimal derivedSalary;

    private List<Bonus> bonus;

    private BigDecimal totalBonus;

    private BigDecimal mandatoryInsurance;

    private BigDecimal taxableIncome;

    private BigDecimal personalIncomeTax;

    private BigDecimal netIncome;
}
