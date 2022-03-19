package com.example.thesis.services;

import com.example.thesis.entities.Attendance;
import com.example.thesis.repositories.AttendanceRepository;
import com.example.thesis.responses.AttendanceResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;

    public List<AttendanceResponse> getAllAttendances(String week, Long departmentId) {
        List<Attendance> attendanceList = null;

        if (week != null) {
            String[] dates = week.split("-");
            String fromDate = dates[0];
            String toDate = dates[1];

            attendanceList = attendanceRepository.findAllAttendancesFromdateTodate(fromDate, toDate);
        } else {
            attendanceList = attendanceRepository.findAll();
        }

        if (departmentId != null) {
            List<Attendance> attendances = attendanceList.stream()
                    .filter(attendance -> (attendance
                            .getEmployee()
                            .getWorks_in()
                            .getDepartment()
                            .getId() == departmentId))
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
                        attendance.getEmployee().getWorks_in().getDepartment().getName(),
                        attendance.getEmployee().getPosition().getName(),
                        new ArrayList<>(attendance.getCheckins()))).collect(Collectors.toList());
    }
}
