package com.example.thesis.repositories;

import com.example.thesis.entities.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

public interface ShiftRepository extends JpaRepository<Shift, Long> {

    @Transactional
    @Modifying
    @Query(value = "update shift set name = ?2, time_in = ?3, time_out = ?4 where id = ?1", nativeQuery = true)
    void setShiftById(Long id, String name, LocalTime timeIn, LocalTime timeOut);
}
