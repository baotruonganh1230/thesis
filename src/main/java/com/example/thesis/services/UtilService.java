package com.example.thesis.services;

import com.example.thesis.entities.Attendance;
import com.example.thesis.entities.Checkin;
import com.example.thesis.entities.Employee;
import com.example.thesis.entities.Leaves;
import com.example.thesis.repositories.AttendanceRepository;
import com.example.thesis.repositories.CheckinRepository;
import com.example.thesis.repositories.EmployeeRepository;
import com.example.thesis.repositories.LeavesRepository;
import com.example.thesis.responses.AttendStatusResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
@AllArgsConstructor
public class UtilService {
    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final CheckinRepository checkinRepository;
    private final LeavesRepository leavesRepository;

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        //convert String to LocalDate
        LocalDate dayPassed = LocalDate.parse(week, formatter);
        LocalDate monday = dayPassed.with(DayOfWeek.MONDAY);
        LocalDate saturday = monday.plusDays(5);

        long onTime = 0;
        long late = 0;
        long onLeave = 0;

        List<Attendance> attendances = attendanceRepository.findAllAttendancesFromdateTodate(monday.toString(), saturday.toString());

        Map<Employee, List<Attendance>> groupByEmployee =
                attendances
                        .stream()
                        .collect(Collectors.groupingBy(Attendance::getEmployee));

        for (Map.Entry<Employee, List<Attendance>> entry : groupByEmployee.entrySet()) {
            boolean isLate = false;
            for (Attendance attendance : entry.getValue()) {
                Checkin checkin = checkinRepository.findByAttendanceId(attendance.getId());
                if (checkin.getTime_in().compareTo(LocalTime.parse("09:00:00.000")) > 0) {
                    isLate = true;
                }
            }
            if (!isLate) {
                onTime += 1;
            } else {
                late += 1;
            }
        }

        List<Leaves> leavesList = leavesRepository.findAllLeavesInWeek(monday.toString(), saturday.toString());

        Map<Employee, List<Leaves>> groupLeavesByEmployee =
                leavesList
                        .stream()
                        .collect(Collectors.groupingBy(Leaves::getEmployee));

        for (Map.Entry<Employee, List<Leaves>> entry : groupLeavesByEmployee.entrySet()) {
            onLeave += 1;
        }

        return new AttendStatusResponse(
                onTime,
                late,
                onLeave
        );
    }
}
