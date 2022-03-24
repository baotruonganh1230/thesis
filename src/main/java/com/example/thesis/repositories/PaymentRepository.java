package com.example.thesis.repositories;

import com.example.thesis.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query(value = "select * from payment where payment_date >= ?1 and payment_date <= ?2 and eid = ?3", nativeQuery = true)
    Payment findPaymentByMonthAndEid(LocalDate firstDate, LocalDate lastDate, Long eid);
}
