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
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentResponse getPayment(String month, Long employeeId) {
        Payment payment = paymentRepository.findPaymentByMonthAndEid(month, employeeId);
        List<Bonus> bonuses = convertListBonus_listToListBonus(payment.getEmployee().getBonus_lists());
        return new PaymentResponse(payment.getBasicSalary(),
                new MonthlyInfo(payment.getActualDay(),
                        payment.getStandardDay(),
                        payment.getPaidLeave(),
                        payment.getUnpaidLeave()),
                payment.getDerivedSalary(),
                bonuses,
                calculateTotalBonus(bonuses),
                payment.getMandatoryInsurance(),
                payment.getTaxableIncome(),
                payment.getPersonalIncomeTax(),
                payment.getNetIncome());
    }

    private List<Bonus> convertListBonus_listToListBonus(List<Bonus_List> bonus_lists) {
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
