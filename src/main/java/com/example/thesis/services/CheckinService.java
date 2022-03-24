package com.example.thesis.services;

import com.example.thesis.entities.Attendance;
import com.example.thesis.entities.Checkin;
import com.example.thesis.entities.Employee;
import com.example.thesis.repositories.AccountRepository;
import com.example.thesis.repositories.AttendanceRepository;
import com.example.thesis.repositories.CheckinRepository;
import com.example.thesis.requests.CheckinRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@AllArgsConstructor
public class CheckinService {
    private final CheckinRepository checkinRepository;
    private final AccountRepository accountRepository;
    private final AttendanceRepository attendanceRepository;

    @Transactional
    public void checkin_in(CheckinRequest checkinRequest) {
        Employee employee = accountRepository.getById(checkinRequest.getUserId()).getEmployee();
        Attendance savedAttendance = attendanceRepository.save(new Attendance(
                null,
                employee,
                checkinRequest.getDate(),
                null
        ));
        checkinRepository.save(
                new Checkin(
                        savedAttendance.getId(),
                        2,
                        checkinRequest.getDate(),
                        checkinRequest.getTimeIn(),
                        null,
                        checkinRequest.getDeviceId(),
                        savedAttendance
                )
        );
    }

    @Transactional
    public void checkin_out(CheckinRequest checkinRequest) {
        Employee employee = accountRepository.getById(checkinRequest.getUserId()).getEmployee();
        Attendance attendance = attendanceRepository.findByEmployeeAndDate(employee, checkinRequest.getDate());
        checkinRepository.setTimeout(attendance.getId(), checkinRequest.getTimeOut());
    }

    public boolean haveCheckedin(Long userId, String date) {
        if (!accountRepository.existsById(userId)) {
            return false;
        }
        Employee employee = accountRepository.getById(userId).getEmployee();
        if (employee == null) return false;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        //convert String to LocalDate
        LocalDate checkinDate = LocalDate.parse(date, formatter);
        Attendance attendance = attendanceRepository.findByEmployeeAndDate(employee, checkinDate);
        if (attendance == null) return false;

        Checkin checkin = checkinRepository.findByAttendanceIdAndDate(attendance.getId(), checkinDate);

        if (checkin != null) return true;
        return false;
    }
}
