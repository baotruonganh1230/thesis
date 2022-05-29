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

    private BigDecimal bonus_v;

    private List<Bonus> bonus;

    private BigDecimal totalBonus;

    private MonthlyInfo monthlyInfo;

    private BigDecimal totalDerivedIncome;

    private BigDecimal derivedSalary;

    private BigDecimal anotherIncome;

    private BigDecimal lunch;

    private BigDecimal parking;

    private BigDecimal totalDeduction;

    private BigDecimal mandatoryInsurance;

    private BigDecimal allowanceNotSubjectedToTax;

    private BigDecimal personalRelief;

    private BigDecimal dependentRelief;

    private BigDecimal taxableIncome;

    private BigDecimal personalIncomeTax;

    private BigDecimal netIncome;
}
