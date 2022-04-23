package com.example.thesis.services;

import com.example.thesis.entities.Attendance;
import com.example.thesis.entities.Employee;
import com.example.thesis.repositories.AttendanceRepository;
import com.example.thesis.responses.AttendanceResponse;
import com.example.thesis.responses.CheckinResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;

    @Transactional
    public List<AttendanceResponse> getAllAttendances(String week, Long departmentId) {
        List<Attendance> attendanceList;
        List<String> workingDays = null;

        if (week != null) {
            workingDays = new ArrayList<>();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

            //convert String to LocalDate
            LocalDate dayPassed = LocalDate.parse(week, formatter);
            LocalDate fromDate = dayPassed.with(DayOfWeek.MONDAY);
            LocalDate toDate = fromDate.plusDays(5);
            for (int i = 0; i < 6; i++) {
                workingDays.add(fromDate.plusDays(i) + "T00:00:00.000Z");
            }

            attendanceList = attendanceRepository.findAllAttendancesFromdateTodate(fromDate.toString(), toDate.toString());
        } else {
            attendanceList = attendanceRepository.findAll();
        }

        if (departmentId != null) {
            List<Long> subDepartmentIds = departmentService.getAllSubDepartmentIdsIncludeThis(departmentId);

            List<Attendance> attendances = attendanceList.stream()
                    .filter(attendance -> (subDepartmentIds.contains(attendance
                            .getEmployee()
                            .getWorksIn()
                            .getDepartment()
                            .getId())))
                    .collect(Collectors.toList());
            List<Employee> employeeList = employeeService.findAllEmployeeByDepartmentIncludeSub(departmentId);
            return getAttendanceResponses(attendances, workingDays, employeeList);
        } else {
            List<Employee> employeeList = employeeService.findAll();
            return getAttendanceResponses(attendanceList, workingDays, employeeList);
        }
    }

    private List<AttendanceResponse> getAttendanceResponses(List<Attendance> attendanceList, List<String> workingDays, List<Employee> employeeList) {
        List<Employee> employeesHaveAttendance = attendanceList.stream()
                .map(Attendance::getEmployee)
                .collect(Collectors.toList());

        for (Employee employee : employeeList) {
            if (!employeesHaveAttendance.contains(employee)) {
                attendanceList.add(new Attendance(
                        null,
                        employee,
                        LocalDate.parse(workingDays.get(0).substring(0,10)),
                        null
                ));
            }
        }
        List<AttendanceResponse> attendanceResponses = convertListAttendanceToAttendanceResponse(attendanceList);
        return addNullAndGroupAttendanceInWeek(attendanceResponses, workingDays);
    }

    private List<AttendanceResponse> convertListAttendanceToAttendanceResponse(List<Attendance> attendanceList) {
        return attendanceList.stream().map(attendance ->
                new AttendanceResponse(attendance.getEmployee().getFirstName() + " " +
                        attendance.getEmployee().getLastName(),
                        attendance.getEmployee().getWorksIn() == null ? null : attendance.getEmployee().getWorksIn().getDepartment().getName(),
                        attendance.getEmployee().getPosition() == null ? null : attendance.getEmployee().getPosition().getName(),
                        attendance.getCheckins() != null ? attendance.getCheckins().stream().map(checkin ->
                                new CheckinResponse(
                                        checkin.getStatus(),
                                        checkin.getDate(),
                                        checkin.getTime_in(),
                                        checkin.getTime_out()
                                )
                        ).collect(Collectors.toList()) : new ArrayList<>(List.of(new CheckinResponse(
                                2,
                                attendance.getDate(),
                                null,
                                null
                        ))))).collect(Collectors.toList());
    }

    private List<AttendanceResponse> addNullAndGroupAttendanceInWeek(
            List<AttendanceResponse> attendanceResponses,
            List<String> workingDays
            ) {

        Map<String, List<AttendanceResponse>> groupByEmployeeName =
                attendanceResponses
                        .stream()
                        .collect(Collectors.groupingBy(AttendanceResponse::getName));
        List<AttendanceResponse> attendanceResponsesReturned = new ArrayList<>();

        for (Map.Entry<String, List<AttendanceResponse>> entry : groupByEmployeeName.entrySet()) {
            List<AttendanceResponse> empAttendance = entry.getValue();
            AttendanceResponse prototype = empAttendance.get(0);
            List<String> avaiableDays = empAttendance
                    .stream()
                    .map(attendanceResponse -> attendanceResponse.getCheckins().get(0).getDate())
                    .collect(Collectors.toList());
            for (int i = 1; i < empAttendance.size(); i++) {
                prototype.getCheckins().add(empAttendance.get(i).getCheckins().get(0));
            }
            if (avaiableDays.size() < 6 && workingDays != null) {
                for (String day : workingDays) {
                    if (!avaiableDays.contains(day)){
                        prototype.getCheckins().add(new CheckinResponse(
                                2,
                                day,
                                null,
                                null
                        ));

                    }
                }
            }
            prototype.getCheckins().sort(Comparator.comparing(CheckinResponse::getDate));
            attendanceResponsesReturned.add(prototype);
        }

        return attendanceResponsesReturned;
    }
}
