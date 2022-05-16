package com.example.thesis.repositories;

import com.example.thesis.entities.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long> {
    @Query(value = "select * from payroll where created_time >= ?1 and created_time <= ?2 limit 1", nativeQuery = true)
    Payroll findPayrollByMonth(LocalDate firstDate, LocalDate lastDate);
}
