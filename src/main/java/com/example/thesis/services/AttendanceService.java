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
            List<AttendanceResponse> attendanceResponsesReturned = new ArrayList<>();
            Map<String, List<AttendanceResponse>> groupByEmployeeName =
                    attendanceResponses
                            .stream()
                            .collect(Collectors.groupingBy(AttendanceResponse::getName));

            for (Map.Entry<String, List<AttendanceResponse>> entry : groupByEmployeeName.entrySet()) {
                AttendanceResponse prototype = entry.getValue().get(0);
                List<String> avaiableDays = entry.getValue()
                    .stream()
                    .map(attendanceResponse -> attendanceResponse.getCheckins().get(0).getDate().toString())
                    .collect(Collectors.toList());
                if (avaiableDays.size() < 6 && workingDays != null) {
                    for (String day : workingDays) {
                        if (!avaiableDays.contains(day)){
                            List<CheckinResponse> checkins = new ArrayList<>();
                            checkins.add(
                                    new CheckinResponse(
                                            null,
                                            prototype.getCheckins().get(0).getStatus(),
                                            day,
                                            null,
                                            null
                                    )
                            );
                            attendanceResponsesReturned.add(
                                    new AttendanceResponse(
                                            prototype.getName(),
                                            prototype.getDepartmentName(),
                                            prototype.getJobTitle(),
                                            checkins
                                    )
                            );
                        } else {
                            attendanceResponsesReturned.add(
                                    attendanceResponses.get(
                                            getIndexByDate(attendanceResponses, day))
                            );
                        }
                    }
                }
            }
            return attendanceResponsesReturned;
        } else {
            return convertListAttendanceToAttendanceResponse(attendanceList);
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
                                        checkin.getAttendanceId(),
                                        checkin.getStatus(),
                                        checkin.getDate(),
                                        checkin.getTime_in(),
                                        checkin.getTime_out()
                                )
                        ).collect(Collectors.toList()))).collect(Collectors.toList());
    }

    private int getIndexByDate(List<AttendanceResponse> attendanceResponses, String date) {
        for (int i = 0; i < attendanceResponses.size(); i++) {
            if (attendanceResponses.get(i).getCheckins().get(0).getDate().equalsIgnoreCase(date)) {
                return i;
            }
        }
        return -1;
    }
}
