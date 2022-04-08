package com.example.thesis.repositories;

import com.example.thesis.entities.Checkin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Repository
public interface CheckinRepository extends JpaRepository<Checkin, Long> {
    Checkin findByAttendanceIdAndDate(Long attendanceId, LocalDate date);

    Checkin findByAttendanceId(Long attendanceId);

    @Transactional
    @Modifying
    @Query(value = "update checkin set time_out = ?2 where attendance_id = ?1", nativeQuery = true)
    int setTimeout(Long attendance_id, String time_out);

    @Transactional
    @Modifying
    @Query(value = "insert into checkin (date, device_id, status, time_in, time_out, attendance_id) values (?3, ?6, ?2, ?4, ?5, ?1)", nativeQuery = true)
    int insertCheckin(Long attendance_id, Integer status, String date, String timeIn, String timeOut, String deviceId);
}
