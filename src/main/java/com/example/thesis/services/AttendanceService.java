package com.example.thesis.services;

import com.example.thesis.entities.Attendance;
import com.example.thesis.entities.Department;
import com.example.thesis.repositories.AttendanceRepository;
import com.example.thesis.repositories.DepartmentRepository;
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
    private final DepartmentRepository departmentRepository;

    @Transactional
    public List<AttendanceResponse> getAllAttendances(String week, Long departmentId) {
        List<Attendance> attendanceList = null;
        List<String> workingDays = null;

        if (week != null) {
            workingDays = new ArrayList<>();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

            //convert String to LocalDate
            LocalDate dayPassed = LocalDate.parse(week, formatter);
            LocalDate fromDate = dayPassed.with(DayOfWeek.MONDAY);
            LocalDate toDate = fromDate.plusDays(5);
            for (int i = 0; i < 6; i++) {
                workingDays.add(fromDate.plusDays(i).toString() + "T00:00:00.000Z");
            }

            attendanceList = attendanceRepository.findAllAttendancesFromdateTodate(fromDate.toString(), toDate.toString());
        } else {
            attendanceList = attendanceRepository.findAll();
        }

        if (departmentId != null) {

            Department department = departmentRepository.getById(departmentId);
            List<Long> subDepartmentIds =
                    departmentRepository.findAllByHeadOfUnit(department)
                            .stream()
                            .map(
                                    Department::getId
                            ).collect(Collectors.toList());

            subDepartmentIds.add(department.getId());

            List<Attendance> attendances = attendanceList.stream()
                    .filter(attendance -> (subDepartmentIds.contains(attendance
                            .getEmployee()
                            .getWorks_in()
                            .getDepartment()
                            .getId())))
                    .collect(Collectors.toList());
            List<AttendanceResponse> attendanceResponses = convertListAttendanceToAttendanceResponse(attendances);

            return addNullAndGroupAttendanceInWeek(attendanceResponses, workingDays);
        } else {
            List<AttendanceResponse> attendanceResponses = convertListAttendanceToAttendanceResponse(attendanceList);
            return addNullAndGroupAttendanceInWeek(attendanceResponses, workingDays);
        }
    }

    private List<AttendanceResponse> convertListAttendanceToAttendanceResponse(List<Attendance> attendanceList) {
        return attendanceList.stream().map(attendance ->
                new AttendanceResponse(attendance.getEmployee().getFirst_name() + " " +
                        attendance.getEmployee().getLast_name(),
                        attendance.getEmployee().getWorks_in() == null ? null : attendance.getEmployee().getWorks_in().getDepartment().getName(),
                        attendance.getEmployee().getPosition() == null ? null : attendance.getEmployee().getPosition().getName(),
                        attendance.getCheckins().stream().map(checkin ->
                                new CheckinResponse(
                                        checkin.getStatus(),
                                        checkin.getDate(),
                                        checkin.getTime_in(),
                                        checkin.getTime_out()
                                )
                        ).collect(Collectors.toList()))).collect(Collectors.toList());
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
                    .map(attendanceResponse -> attendanceResponse.getCheckins().get(0).getDate().toString())
                    .collect(Collectors.toList());
            for (int i = 1; i < empAttendance.size(); i++) {
                prototype.getCheckins().add(empAttendance.get(i).getCheckins().get(0));
            }
            if (avaiableDays.size() < 6 && workingDays != null) {
                for (String day : workingDays) {
                    if (!avaiableDays.contains(day)){
                        prototype.getCheckins().add(new CheckinResponse(
                                prototype.getCheckins().get(0).getStatus(),
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
