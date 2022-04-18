package com.example.thesis.services;

import com.example.thesis.entities.Attendance;
import com.example.thesis.entities.Checkin;
import com.example.thesis.entities.Employee;
import com.example.thesis.entities.Payroll;
import com.example.thesis.repositories.AttendanceRepository;
import com.example.thesis.repositories.CheckinRepository;
import com.example.thesis.repositories.EmployeeRepository;
import com.example.thesis.repositories.PayrollRepository;
import com.example.thesis.responses.AttendStatusResponse;
import com.example.thesis.responses.PaymentResponse;
import com.example.thesis.responses.PayrollEntity;
import com.example.thesis.responses.PayrollResponsePdf;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        LocalDate monday = dayPassed.with(DayOfWeek.MONDAY);
        LocalDate saturday = monday.plusDays(5);

        List<Employee> employees = employeeRepository.findAll();
        Map<Long, Double> averageHours = new HashMap<>();
        for (Employee employee : employees) {
            averageHours.put(employee.getId(), 0.0);
        }

        List<Attendance> attendances = attendanceRepository.findAllAttendancesFromdateTodate(monday.toString(), saturday.toString());

        Map<Employee, List<Attendance>> groupByEmployee =
                attendances
                        .stream()
                        .collect(Collectors.groupingBy(Attendance::getEmployee));

        for (Map.Entry<Employee, List<Attendance>> entry : groupByEmployee.entrySet()) {
            long sumHours = 0L;
            for (Attendance attendance : entry.getValue()) {
                Checkin checkin = checkinRepository.findByAttendanceId(attendance.getId());
                long workingHours = HOURS.between(checkin.getTime_in(), checkin.getTime_out());
                sumHours += workingHours - 1;
            }
            averageHours.put(entry.getKey().getId(), (double) sumHours / (double) entry.getValue().size());
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
                    if (checkin.getTime_in().compareTo(LocalTime.parse("09:00:00.000")) > 0) {
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

    @Transactional
    public PayrollResponsePdf getPayrollPdf(String month) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        //convert String to LocalDate
        LocalDate dayPassed = LocalDate.parse(month, formatter);
        LocalDate firstDate = dayPassed.withDayOfMonth(1);
        LocalDate lastDate = dayPassed.withDayOfMonth(
                dayPassed.getMonth().length(dayPassed.isLeapYear()));

        Payroll payroll = payrollRepository.findPayrollByMonth(firstDate, lastDate);
        List<PayrollEntity> payrollEntities =
                payroll.getPayments()
                        .stream()
                        .map(payment ->
                        {
                            PaymentResponse paymentResponse =
                                    paymentService.getPayment(month, payment.getEid());

                            return new PayrollEntity(
                                    payment.getEid(),
                                    payment.getEmployee().getFirstName(),
                                    payment.getEmployee().getLastName(),
                                    payment.getPayment_date().format(DateTimeFormatter.ofPattern("MM/yyyy")),
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
}
