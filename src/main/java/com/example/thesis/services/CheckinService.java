package com.example.thesis.services;

import com.example.thesis.entities.Attendance;
import com.example.thesis.entities.Checkin;
import com.example.thesis.entities.Employee;
import com.example.thesis.repositories.AccountRepository;
import com.example.thesis.repositories.AttendanceRepository;
import com.example.thesis.repositories.CheckinRepository;
import com.example.thesis.requests.CheckinRequest;
import com.example.thesis.responses.HaveCheckedInResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
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

        Attendance attendance = attendanceRepository.findByEmployeeAndDate(employee, checkinRequest.getDate());

        if (!attendanceRepository.existsByEmployeeAndDate(employee, checkinRequest.getDate())) {
            Attendance savedAttendance = attendanceRepository.save(new Attendance(
                    null,
                    employee,
                    checkinRequest.getDate(),
                    null
            ));

            checkinRepository.insertCheckin(
                    savedAttendance.getId(),
                    2,
                    checkinRequest.getDate().toString(),
                    checkinRequest.getTimeIn().toString(),
                    null,
                    checkinRequest.getDeviceId()
            );
        }

    }

    @Transactional
    public void checkin_out(CheckinRequest checkinRequest) {
        Employee employee = accountRepository.getById(checkinRequest.getUserId()).getEmployee();
        Attendance attendance = attendanceRepository.findByEmployeeAndDate(employee, checkinRequest.getDate());
        Checkin checkin = checkinRepository.findByAttendanceId(attendance.getId());
        checkin.setTime_out(checkinRequest.getTimeOut());
        Checkin savedCheckin = checkinRepository.save(checkin);
        int status;

        if (savedCheckin.getTime_in().compareTo(LocalTime.parse("09:00:00.000")) <= 0 && checkinRequest.getTimeOut().compareTo(LocalTime.parse("18:00:00.000")) >= 0) {
            status = 0;
        } else if (savedCheckin.getTime_in().compareTo(LocalTime.parse("09:00:00.000")) > 0 || checkinRequest.getTimeOut().compareTo(LocalTime.parse("18:00:00.000")) < 0) {
            status = 1;
        } else {
            status = 2;
        }
        checkinRepository.setStatus(attendance.getId(), status);
    }

    public HaveCheckedInResponse haveCheckedin(Long userId, String date) {
        if (!accountRepository.existsById(userId)) {
            return new HaveCheckedInResponse(false, null, null);
        }
        Employee employee = accountRepository.getById(userId).getEmployee();
        if (employee == null) return new HaveCheckedInResponse(false, null, null);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        //convert String to LocalDate
        LocalDate checkinDate = LocalDate.parse(date, formatter);
        Attendance attendance = attendanceRepository.findByEmployeeAndDate(employee, checkinDate);
        if (attendance == null) return new HaveCheckedInResponse(false, null, null);

        Checkin checkin = checkinRepository.findByAttendanceIdAndDate(attendance.getId(), checkinDate);

        if (checkin != null) return new HaveCheckedInResponse(true,
                checkin.getDate().toString() + "T" + checkin.getTime_in().toString() + "Z",
                checkin.getTime_out() == null ? null : checkin.getDate().toString() + "T" + checkin.getTime_out().toString() + "Z");
        return new HaveCheckedInResponse(false, null, null);
    }
}
