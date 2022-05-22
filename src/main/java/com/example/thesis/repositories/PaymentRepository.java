package com.example.thesis.repositories;

import com.example.thesis.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query(value = "select * from payment where payment_date >= ?1 and payment_date <= ?2 and eid = ?3", nativeQuery = true)
    Payment findPaymentByMonthAndEid(LocalDate firstDate, LocalDate lastDate, Long eid);

    @Transactional
    @Modifying
    @Query(value = "UPDATE payment " +
            "SET lunch = ?2 WHERE id = ?1", nativeQuery = true)
    int updateLunchById(Long id, BigDecimal lunch);

    @Transactional
    @Modifying
    @Query(value = "UPDATE payment " +
            "SET parking = ?2 WHERE id = ?1", nativeQuery = true)
    int updateParkingById(Long id, BigDecimal parking);

    @Transactional
    @Modifying
    @Query(value = "UPDATE payment " +
            "SET basic_salary = ?2 WHERE id = ?1", nativeQuery = true)
    int updateBasic_salaryById(Long id, BigDecimal basic_salary);
}
