package com.example.thesis.repositories;

import com.example.thesis.entities.PaymentBonusRel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PaymentBonusRelRepository extends JpaRepository<PaymentBonusRel, Long> {
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO `payment_bonus_rel` (`bonus_id`, `payment_id`) VALUES (?1, ?2);", nativeQuery = true)
    int insertPaymentBonusRel(Long bonusid, Long paymentid);

    @Query(value = "select * from `payment_bonus_rel` where `payment_id` = ?1", nativeQuery = true)
    List<PaymentBonusRel> getPaymentBonusRelsByPaymentId(Long paymentid);
}
