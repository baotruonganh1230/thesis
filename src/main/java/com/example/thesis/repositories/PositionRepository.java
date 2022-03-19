package com.example.thesis.repositories;

import com.example.thesis.entities.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {
    @Transactional
    @Modifying
    @Query(value = "update position set salary_group = ?2 where id = ?1", nativeQuery = true)
    void setSalaryGroupById(Long id, Integer salaryGroup);

    @Transactional
    @Modifying
    @Query(value = "update position set name = ?2, description = ?3, note = ?4, min_salary = ?5, max_salary = ?6, salary_group = ?7 where id = ?1", nativeQuery = true)
    void setPositionById(Long id, String name, String description, String note, BigDecimal min_salary, BigDecimal max_salary, Integer salary_group);
}
