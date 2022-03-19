package com.example.thesis.repositories;

import com.example.thesis.entities.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    @Query(value = "select * from attendance where day > ?1 AND day < ?2", nativeQuery = true)
    List<Attendance> findAllAttendancesFromdateTodate(String fromDate, String toDate);
}
