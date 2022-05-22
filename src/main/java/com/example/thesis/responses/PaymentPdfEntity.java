package com.example.thesis.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentPdfEntity {
    private Long employeeId;
    private String employeeName;
    private String department;
    private String month;
    private BigDecimal basicSalary;
    private BigDecimal bonus;
    private String standardDays;
    private String actualDays;
    private String unpaidLeave;
    private String paidLeave;
    private BigDecimal totalDerivedIncome;
    private BigDecimal derivedSalary;
    private BigDecimal anotherIncome;
    private BigDecimal lunch;
    private BigDecimal parking;
    private BigDecimal totalDeduction;
    private BigDecimal mandatoryInsurance;
    private BigDecimal personalIncomeTax;
    private BigDecimal allowanceNotSubjectedToTax;
    private BigDecimal personalRelief;
    private BigDecimal dependentRelief;
    private BigDecimal taxableIncome;
    private BigDecimal netIncome;
}
