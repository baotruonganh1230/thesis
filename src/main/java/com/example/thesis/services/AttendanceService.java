package com.example.thesis.services;

import com.example.thesis.entities.Attendance;
import com.example.thesis.entities.Department;
import com.example.thesis.repositories.AttendanceRepository;
import com.example.thesis.repositories.DepartmentRepository;
import com.example.thesis.responses.AttendanceResponse;
import com.example.thesis.responses.CheckinResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final DepartmentRepository departmentRepository;

    public List<AttendanceResponse> getAllAttendances(String week, Long departmentId) {
        List<Attendance> attendanceList = null;

        if (week != null) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

            //convert String to LocalDate
            LocalDate dayPassed = LocalDate.parse(week, formatter);
            LocalDate fromDate = dayPassed.with(DayOfWeek.MONDAY);
            LocalDate toDate = fromDate.plusDays(5);

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
                                    department1 ->
                                            department1.getId()
                            ).collect(Collectors.toList());

            subDepartmentIds.add(department.getId());

            List<Attendance> attendances = attendanceList.stream()
                    .filter(attendance -> (subDepartmentIds.contains(attendance
                            .getEmployee()
                            .getWorks_in()
                            .getDepartment()
                            .getId())))
                    .collect(Collectors.toList());
            return convertListAttendanceToAttendanceResponse(attendances);
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
}
