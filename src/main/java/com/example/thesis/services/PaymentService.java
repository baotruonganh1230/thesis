package com.example.thesis.services;

import com.example.thesis.entities.Bonus_List;
import com.example.thesis.entities.Payment;
import com.example.thesis.repositories.PaymentRepository;
import com.example.thesis.responses.Bonus;
import com.example.thesis.responses.MonthlyInfo;
import com.example.thesis.responses.PaymentResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentResponse getPayment(String month, Long employeeId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        //convert String to LocalDate
        LocalDate dayPassed = LocalDate.parse(month, formatter);
        LocalDate firstDate = dayPassed.withDayOfMonth(1);
        LocalDate lastDate = dayPassed.withDayOfMonth(
                dayPassed.getMonth().length(dayPassed.isLeapYear()));

        Payment payment = paymentRepository.findPaymentByMonthAndEid(firstDate, lastDate, employeeId);
        if (payment == null) {
            return null;
        }
        List<Bonus> bonuses = convertListBonus_listToListBonus(payment.getEmployee().getBonus_lists());
        BigDecimal totalBonus = calculateTotalBonus(bonuses);
        BigDecimal derivedSalary = payment.getBasicSalary()
                .add(totalBonus)
                .divide(new BigDecimal(payment.getStandardDay()), RoundingMode.HALF_UP)
                .multiply(new BigDecimal(payment.getActualDay()));

        BigDecimal anotherIncome = payment.getLunch().add(payment.getParking());
        BigDecimal totalDerivedIncome = derivedSalary.add(anotherIncome);
        BigDecimal mandatoryInsurance = totalDerivedIncome
                .multiply(new BigDecimal("10.5"))
                .divide(new BigDecimal("100"), RoundingMode.HALF_UP);
        BigDecimal taxableIncome = totalDerivedIncome
                .subtract(mandatoryInsurance)
                .subtract(payment.getAllowanceNotSubjectedToTax())
                .subtract(payment.getPersonalRelief())
                .subtract(payment.getDependentRelief());
        BigDecimal personalIncomeTax = taxableIncome.divide(new BigDecimal("10"), RoundingMode.HALF_UP);
        BigDecimal totalDeduction = mandatoryInsurance.add(personalIncomeTax);
        BigDecimal netIncome = totalDerivedIncome.subtract(totalDeduction);
        return new PaymentResponse(payment.getBasicSalary(),
                bonuses,
                totalBonus,
                new MonthlyInfo(payment.getActualDay(),
                        payment.getStandardDay(),
                        payment.getPaidLeave(),
                        payment.getUnpaidLeave()),
                totalDerivedIncome,
                derivedSalary,
                anotherIncome,
                payment.getLunch(),
                payment.getParking(),
                totalDeduction,
                mandatoryInsurance,
                payment.getAllowanceNotSubjectedToTax(),
                payment.getPersonalRelief(),
                payment.getDependentRelief(),
                taxableIncome,
                personalIncomeTax,
                netIncome);
    }

    private List<Bonus> convertListBonus_listToListBonus(Set<Bonus_List> bonus_lists) {
        return bonus_lists.stream().map(bonus_list ->
                new Bonus(bonus_list.getId(),
                        bonus_list.getName(),
                        bonus_list.getAmount()))
                .collect(Collectors.toList());
    }

    private BigDecimal calculateTotalBonus(List<Bonus> bonuses) {
        return bonuses.stream()
                .map(Bonus::getBonusAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
