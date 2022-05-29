package com.example.thesis.repositories;

import com.example.thesis.entities.Attendance;
import com.example.thesis.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Attendance findByEmployeeAndDate(Employee employee, LocalDate date);

    @Query(value = "select * from attendance where date >= ?1 AND date <= ?2", nativeQuery = true)
    List<Attendance> findAllAttendancesFromdateTodate(String fromDate, String toDate);

    @Query(value = "select * from attendance where date >= ?2 AND date <= ?3 AND eid = ?1", nativeQuery = true)
    List<Attendance> findAllAttendancesOfEmployeeFromdateTodate(Long eid, String fromDate, String toDate);

    @Query(value = "select * from attendance where date = ?2 AND eid = ?1 order by id desc limit 1", nativeQuery = true)
    Attendance findAttendanceOfEmployeeByDate(Long eid, LocalDate date);

    boolean existsByEmployeeAndDate(Employee employee, LocalDate date);
}
