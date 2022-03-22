package com.example.thesis.repositories;

import com.example.thesis.entities.Insurance;
import com.example.thesis.keys.InsurancePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

public interface InsuranceRepository extends JpaRepository<Insurance, InsurancePK> {
    @Transactional
    @Modifying
    @Query(value = "insert into insurance (eid, typeid, city_id, from_date, issue_date, kcb_id, number, to_date) values (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8)", nativeQuery = true)
    int insertInsurance(Long eid, Long typeid, Long city_id, LocalDate from_date, LocalDate issue_date, Long kcb_id, String number, LocalDate to_date);

    @Transactional
    @Modifying
    @Query(value = "update insurance set city_id = ?3, from_date = ?4, issue_date = ?5, kcb_id = ?6, number = ?7, to_date = ?8 where eid = ?1 AND typeid = ?2", nativeQuery = true)
    int updateInsurance(Long eid, Long typeid, Long city_id, LocalDate from_date, LocalDate issue_date, Long kcb_id, String number, LocalDate to_date);

}
