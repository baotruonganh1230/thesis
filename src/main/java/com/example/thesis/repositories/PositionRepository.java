package com.example.thesis.repositories;

import com.example.thesis.entities.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {
    @Transactional
    @Modifying
    @Query(value = "update Position p set p.salary_group = ?2 where p.id = ?1", nativeQuery = true)
    void setSalaryGroupById(Long id, Integer salaryGroup);

    @Transactional
    @Modifying
    @Query(value = "update Position p set p.title = ?2, p.description = ?3, p.note = ?4 where p.id = ?1", nativeQuery = true)
    void setPositionById(Long id, String title, String description, String note);
}
