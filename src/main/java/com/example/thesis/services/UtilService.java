package com.example.thesis.services;

import com.example.thesis.entities.Attendance;
import com.example.thesis.entities.Checkin;
import com.example.thesis.entities.Employee;
import com.example.thesis.entities.Payroll;
import com.example.thesis.repositories.AttendanceRepository;
import com.example.thesis.repositories.CheckinRepository;
import com.example.thesis.repositories.EmployeeRepository;
import com.example.thesis.repositories.PayrollRepository;
import com.example.thesis.responses.*;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.HOURS;

@Service
public class UtilService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private CheckinRepository checkinRepository;

    @Autowired
    private PayrollRepository payrollRepository;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private GoogleDriveService googleDriveService;

    @Autowired
    private PdfGenerateService pdfGenerateService;

    @Value("${pdf.directory}")
    private String pdfDirectory;

    public List<Double> getAverageWorkingHoursByWeek(String week) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        //convert String to LocalDate
        LocalDate dayPassed = LocalDate.parse(week, formatter);
        List<LocalDate> workingDays = new ArrayList<>();

        LocalDate monday = dayPassed.with(DayOfWeek.MONDAY);
        LocalDate saturday = monday.plusDays(5);
        for (int i = 0; i < 6; i++) {
            workingDays.add(monday.plusDays(i));
        }

        long employeeCount = employeeRepository.findAll().size();
        Map<LocalDate, Double> averageHours = new TreeMap<>();
        for (LocalDate date : workingDays) {
            averageHours.put(date, 0.0);
        }

        List<Attendance> attendances = attendanceRepository.findAllAttendancesFromdateTodate(monday.toString(), saturday.toString());

        Map<LocalDate, List<Attendance>> groupByDate =
                attendances
                        .stream()
                        .collect(Collectors.groupingBy(Attendance::getDate));

        for (Map.Entry<LocalDate, List<Attendance>> entry : groupByDate.entrySet()) {
            long sumHours = 0L;
            for (Attendance attendance : entry.getValue()) {
                Checkin checkin = checkinRepository.findByAttendanceId(attendance.getId());
                long workingHours = 1;
                if (checkin.getTime_out() != null) {
                    workingHours = HOURS.between(checkin.getTime_in(), checkin.getTime_out());
                }
                sumHours += workingHours - 1;
            }
            averageHours.put(entry.getKey(), (double) sumHours / (double) employeeCount);
        }

        return new ArrayList<>(averageHours.values());
    }

    public AttendStatusResponse getEmployeeAttendStatus(String week) {
        List<String> workingDays = new ArrayList<>();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        LocalDate dayPassed = LocalDate.parse(week, formatter);
        LocalDate monday = dayPassed.with(DayOfWeek.MONDAY);
        for (int i = 0; i < 6; i++) {
            workingDays.add(monday.plusDays(i) + "T00:00:00.000Z");
        }

        List<Long> onTime = new ArrayList<>();
        List<Long> late = new ArrayList<>();
        List<Long> off = new ArrayList<>();

        List<Employee> employees = employeeRepository.findAll();

        for (String date : workingDays) {
            long onTimeThatDay = 0;
            long lateThatDay = 0;
            long offThatDay = 0;

            for (Employee employee : employees) {
                Attendance attendance = attendanceRepository.findByEmployeeAndDate(employee, LocalDate.parse(date.substring(0,10)));
                if (attendance != null) {
                    Checkin checkin = checkinRepository.findByAttendanceId(attendance.getId());
                    if (checkin.getTime_in().compareTo(employee.getShift().getTimeIn()) > 0) {
                        lateThatDay += 1;
                    } else {
                        onTimeThatDay += 1;
                    }
                } else {
                    offThatDay += 1;
                }
            }
            onTime.add(onTimeThatDay);
            late.add(lateThatDay);
            off.add(offThatDay);
        }

        return new AttendStatusResponse(
                onTime,
                late,
                off
        );
    }

    public List<BigDecimal> getTotalPaymentValues(String year) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        //convert String to LocalDate
        LocalDate dayPassed = LocalDate.parse(year, formatter);

        LocalDate firstDayOfYear = LocalDate.of(dayPassed.getYear(), Month.JANUARY, 1);
        List<LocalDate> months = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            months.add(firstDayOfYear.plusMonths(i));
        }
        Map<LocalDate, BigDecimal> sortedMap = new TreeMap<>();
        for (LocalDate month : months) {
            sortedMap.put(month, BigDecimal.ZERO);
        }

        for (LocalDate month : months) {
            List<Employee> employees = employeeRepository.findAll();
            BigDecimal totalNetIncome = BigDecimal.ZERO;
            for (Employee employee : employees) {
                PaymentResponse paymentResponse =
                        paymentService.getPayment(month.toString() + "T00:00:00.000Z",
                                employee.getId());
                totalNetIncome = totalNetIncome.add(paymentResponse.getNetIncome());
            }
            sortedMap.put(month, totalNetIncome);
        }

        return new ArrayList<>(sortedMap.values());
    }


    public PayrollResponsePdf getPayrollPdf(String month) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        //convert String to LocalDate
        LocalDate dayPassed = LocalDate.parse(month, formatter);
        LocalDate firstDate = dayPassed.withDayOfMonth(1);
        LocalDate lastDate = dayPassed.withDayOfMonth(
                dayPassed.getMonth().length(dayPassed.isLeapYear()));

        List<Employee> allEmployees = employeeRepository.findAll();

        for (Employee employee : allEmployees) {
            paymentService.getPayment(month, employee.getId());
        }

        Payroll payroll = payrollRepository.findPayrollByMonth(firstDate, lastDate);
        List<PayrollEntity> payrollEntities =
                payroll.getPayments()
                        .stream()
                        .map(payment ->
                        {
                            PaymentResponse paymentResponse =
                                    paymentService.getPayment(month, payment.getEmployee().getId());

                            return new PayrollEntity(
                                    payment.getEmployee().getId(),
                                    payment.getEmployee().getFirstName(),
                                    payment.getEmployee().getLastName(),
                                    payment.getPaymentDate().format(DateTimeFormatter.ofPattern("MM/yyyy")),
                                    paymentResponse.getNetIncome()
                            );
                        })
                        .collect(Collectors.toList());

        Map<String, Object> data = new HashMap<>();
        data.put("entities", payrollEntities);

        pdfGenerateService.generatePdfFile("payroll", data, "payroll.pdf");
        File filetoUpload = new File(pdfDirectory + "payroll.pdf");

        Tika tika = new Tika();
        String mimeType = null;
        try {
            mimeType = tika.detect(filetoUpload);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("The file name is: " + filetoUpload.getName());
        System.out.println("The file mime type is: " + mimeType);

        com.google.api.services.drive.model.File upLoadedFile =
                googleDriveService.upLoadFile(filetoUpload.getName(),
                        filetoUpload.getAbsolutePath(), mimeType);

        if (!filetoUpload.delete()) {
            throw new IllegalStateException("Cannot delete file!");
        }

        return new PayrollResponsePdf(
                upLoadedFile.getWebContentLink()
        );

    }

    public PaymentResponsePdf getPaymentPdf(Long id, String month) {

        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (employeeOptional.isEmpty()) {
            throw new EntityNotFoundException("There is no employee with that id!");
        }

        Employee employee = employeeOptional.get();

        PaymentResponse paymentResponse = paymentService.getPayment(month, employee.getId());

        Map<String, Object> data = new HashMap<>();
        data.put("item", new PaymentPdfEntity(
                employee.getId(),
                employee.getFirstName() + " " + employee.getLastName(),
                employee.getWorksIn().getDepartment().getName(),
                LocalDate.parse(month.substring(0,10)).format(DateTimeFormatter.ofPattern("MM/yyyy")),
                paymentResponse.getBasicSalary(),
                paymentResponse.getBonus(),
                paymentResponse.getMonthlyInfo().getStandardDay(),
                paymentResponse.getMonthlyInfo().getActualDay(),
                paymentResponse.getMonthlyInfo().getUnpaidLeave(),
                paymentResponse.getMonthlyInfo().getPaidLeave(),
                paymentResponse.getTotalDerivedIncome(),
                paymentResponse.getDerivedSalary(),
                paymentResponse.getAnotherIncome(),
                paymentResponse.getLunch(),
                paymentResponse.getParking(),
                paymentResponse.getTotalDeduction(),
                paymentResponse.getMandatoryInsurance(),
                paymentResponse.getPersonalIncomeTax(),
                paymentResponse.getAllowanceNotSubjectedToTax(),
                paymentResponse.getPersonalRelief(),
                paymentResponse.getDependentRelief(),
                paymentResponse.getTaxableIncome(),
                paymentResponse.getNetIncome()
        ));

        pdfGenerateService.generatePdfFile("payment", data, "payment.pdf");
        File filetoUpload = new File(pdfDirectory + "payment.pdf");

        Tika tika = new Tika();
        String mimeType = null;
        try {
            mimeType = tika.detect(filetoUpload);
        } catch (IOException e) {
            e.printStackTrace();
        }

        com.google.api.services.drive.model.File upLoadedFile =
                googleDriveService.upLoadFile(filetoUpload.getName(),
                        filetoUpload.getAbsolutePath(), mimeType);

        if (!filetoUpload.delete()) {
            throw new IllegalStateException("Cannot delete file!");
        }

        return new PaymentResponsePdf(
                upLoadedFile.getWebContentLink()
        );

    }
}
