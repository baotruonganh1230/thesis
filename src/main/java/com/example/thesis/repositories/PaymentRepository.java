package com.example.thesis.repositories;

import com.example.thesis.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query(value = "select * from payment where month(payment_date) = ?1 AND eid = ?2", nativeQuery = true)
    Payment findPaymentByMonthAndEid(String month, Long eid);
}
