package com.example.thesis.services;

import com.example.thesis.entities.*;
import com.example.thesis.repositories.*;
import com.example.thesis.responses.Bonus;
import com.example.thesis.responses.MonthlyInfo;
import com.example.thesis.responses.PaymentResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@AllArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final LeavesRepository leavesRepository;
    private final PayrollRepository payrollRepository;

    @Transactional
    public PaymentResponse getPayment(String month, Long employeeId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        //convert String to LocalDate
        LocalDate dayPassed = LocalDate.parse(month, formatter);
        LocalDate firstDate = dayPassed.withDayOfMonth(1);
        LocalDate lastDate = dayPassed.withDayOfMonth(
                dayPassed.getMonth().length(dayPassed.isLeapYear()));

        Payment payment = paymentRepository.findPaymentByMonthAndEid(firstDate, lastDate, employeeId);

        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
        if (employeeOptional.isEmpty()) {
            throw new IllegalStateException("Employee not exist");
        }
        Employee employee = employeeOptional.get();

        if (payment == null) {

            List<Attendance> attendancesInMonth =
                    attendanceRepository.findAllAttendancesOfEmployeeFromdateTodate(
                            employeeId,
                            firstDate.toString(),
                            lastDate.toString());

            String actualDay = String.valueOf(attendancesInMonth.size());
            String standardDay = String.valueOf(countWorkingDaysOfMonth(firstDate, lastDate));

            BigDecimal basicSalary = employee.getGross_salary();

            List<Leaves> listLeavesOverlap = leavesRepository.findAllByEmployeeAndPeriod(employeeId, firstDate, lastDate);
            long paidLeave = 0;
            for (Leaves leaves : listLeavesOverlap) {
                LocalDate laterStart = Collections.max(Arrays.asList(leaves.getFromDate(), firstDate));
                LocalDate earlierEnd = Collections.min(Arrays.asList(leaves.getToDate(), lastDate));
                long overlappingDays = ChronoUnit.DAYS.between(laterStart, earlierEnd) + 1;
                if (leaves.getType().getIs_paid()) {
                    paidLeave += overlappingDays;
                }
            }

            long unpaidLeave = Long.parseLong(standardDay) -
                    Long.parseLong(actualDay) -
                    paidLeave;

            Payroll payroll = payrollRepository.findPayrollByMonth(firstDate, lastDate);

            if (payroll == null) {
                payroll = payrollRepository.save(
                        new Payroll(
                                null,
                                "Payroll_For_" + dayPassed,
                                dayPassed,
                                null
                        )
                );
            }
            BigDecimal lunch;
            Optional<Bonus_List> optionalLunch = employee.getBonus_lists()
                    .stream()
                    .filter(bonus_list -> bonus_list.getBonusName().equalsIgnoreCase("Lunch"))
                    .findFirst();
            if (optionalLunch.isPresent()) {
                lunch = optionalLunch.get().getAmount();
            } else {
                lunch = BigDecimal.valueOf(0.00);
            }

            BigDecimal parking;
            Optional<Bonus_List> optionalParking = employee.getBonus_lists()
                    .stream()
                    .filter(bonus_list -> bonus_list.getBonusName().equalsIgnoreCase("Parking"))
                    .findFirst();
            if (optionalParking.isPresent()) {
                parking = optionalParking.get().getAmount();
            } else {
                parking = BigDecimal.valueOf(0.00);
            }

            BigDecimal allowanceNotSubjectedToTax = BigDecimal.valueOf(730000.00);
            BigDecimal personalRelief = BigDecimal.valueOf(11000000.00);
            BigDecimal dependentRelief = BigDecimal.valueOf(0.00);

            payment = paymentRepository.save(
                    new Payment(
                            null,
                            employee,
                            basicSalary,
                            dayPassed,
                            actualDay,
                            standardDay,
                            String.valueOf(paidLeave),
                            String.valueOf(unpaidLeave),
                            lunch,
                            parking,
                            allowanceNotSubjectedToTax,
                            personalRelief,
                            dependentRelief,
                            payroll
                    )
            );
        }

        BigDecimal basicSalary = payment.getBasicSalary();
        if (!employee.getGross_salary().equals(payment.getBasicSalary())) {
            paymentRepository.updateBasic_salaryById(payment.getId(), employee.getGross_salary());
            basicSalary = employee.getGross_salary();
        }

        List<Bonus> bonuses = convertListBonus_listToListBonus(payment.getEmployee().getBonus_lists());
        List<Bonus> bonusesExcludeLunchAndParking = bonuses
                .stream()
                .filter(bonus ->
                        ((!bonus.getBonusName().equalsIgnoreCase("Lunch"))
                                && (!bonus.getBonusName().equalsIgnoreCase("Parking"))))
                .collect(Collectors.toList());
        BigDecimal totalBonus = calculateTotalBonus(bonusesExcludeLunchAndParking);
        BigDecimal derivedSalary = basicSalary
                .add(totalBonus)
                .multiply(new BigDecimal(payment.getActualDay()).add(new BigDecimal(payment.getPaidLeave())))
                .divide(new BigDecimal(payment.getStandardDay()), RoundingMode.HALF_UP);

        BigDecimal lunch = payment.getLunch();
        Optional<Bonus_List> optionalLunch = employee.getBonus_lists()
                .stream()
                .filter(bonus_list -> bonus_list.getBonusName().equalsIgnoreCase("Lunch"))
                .findFirst();
        if (optionalLunch.isPresent() && !optionalLunch.get().getAmount().equals(payment.getLunch())) {
            paymentRepository.updateLunchById(payment.getId(), optionalLunch.get().getAmount());
            lunch = optionalLunch.get().getAmount();
        }

        BigDecimal parking = payment.getParking();
        Optional<Bonus_List> optionalParking = employee.getBonus_lists()
                .stream()
                .filter(bonus_list -> bonus_list.getBonusName().equalsIgnoreCase("Parking"))
                .findFirst();
        if (optionalParking.isPresent() && !optionalParking.get().getAmount().equals(payment.getParking())) {
            paymentRepository.updateParkingById(payment.getId(), optionalParking.get().getAmount());
            parking = optionalParking.get().getAmount();
        }

        BigDecimal anotherIncome = lunch.add(parking);
        BigDecimal totalDerivedIncome = derivedSalary.add(anotherIncome);
        BigDecimal mandatoryInsurance = totalDerivedIncome
                .multiply(new BigDecimal("11.5"))
                .divide(new BigDecimal("100"), RoundingMode.HALF_UP);

        BigDecimal temporalValue = anotherIncome.subtract(payment.getAllowanceNotSubjectedToTax());
        BigDecimal taxableIncome;

        if (temporalValue.compareTo(BigDecimal.ZERO) > 0) {
            taxableIncome = totalDerivedIncome
                .subtract(mandatoryInsurance)
                .subtract(payment.getAllowanceNotSubjectedToTax())
                .subtract(payment.getPersonalRelief())
                .subtract(payment.getDependentRelief());
        } else {
            taxableIncome = derivedSalary
                    .subtract(payment.getPersonalRelief())
                    .subtract(payment.getDependentRelief());
        }
        if (taxableIncome.compareTo(BigDecimal.ZERO) < 0) {
            taxableIncome = BigDecimal.valueOf(0.00);
        }
        BigDecimal personalIncomeTax = taxableIncome.divide(new BigDecimal("10"), RoundingMode.HALF_UP);
        BigDecimal totalDeduction = mandatoryInsurance.add(personalIncomeTax);
        BigDecimal netIncome = totalDerivedIncome.subtract(totalDeduction);
        if (netIncome.compareTo(BigDecimal.ZERO) < 0) {
            netIncome = BigDecimal.valueOf(0.00);
        }
        return new PaymentResponse(basicSalary,
                bonuses,
                totalBonus,
                new MonthlyInfo(payment.getActualDay(),
                        payment.getStandardDay(),
                        payment.getPaidLeave(),
                        payment.getUnpaidLeave()),
                totalDerivedIncome,
                derivedSalary,
                anotherIncome,
                lunch,
                parking,
                totalDeduction,
                mandatoryInsurance,
                payment.getAllowanceNotSubjectedToTax(),
                payment.getPersonalRelief().setScale(2),
                payment.getDependentRelief(),
                taxableIncome,
                personalIncomeTax,
                netIncome);

    }

    private List<Bonus> convertListBonus_listToListBonus(Set<Bonus_List> bonus_lists) {
        return bonus_lists.stream().map(bonus_list ->
                new Bonus(bonus_list.getId(),
                        bonus_list.getBonusName(),
                        bonus_list.getAmount()))
                .collect(Collectors.toList());
    }

    private BigDecimal calculateTotalBonus(List<Bonus> bonuses) {
        return bonuses.stream()
                .map(Bonus::getBonusAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private int countWorkingDaysOfMonth(LocalDate firstDate, LocalDate lastDate) {
        int totalDays = Math.toIntExact(DAYS.between(firstDate, lastDate) + 1);
        int returnCount = 0;
        for (int i = 0; i < totalDays; i++) {
            if (!firstDate.plusDays(i)
                    .getDayOfWeek()
                    .toString().equalsIgnoreCase("SUNDAY")) {
                returnCount++;
            }
        }
        return returnCount;
    }
}
