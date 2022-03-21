package com.example.thesis.repositories;

import com.example.thesis.entities.Checkin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public interface CheckinRepository extends JpaRepository<Checkin, Long> {
    Checkin findByAttendanceIdAndDate(Long attendanceId, LocalDate date);

    @Transactional
    @Modifying
    @Query(value = "update checkin set time_out = ?2 where attendance_id = ?1", nativeQuery = true)
    int setTimeout(Long attendance_id, LocalTime time_out);
}
