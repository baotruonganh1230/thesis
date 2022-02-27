package com.example.thesis.repositories;

import com.example.thesis.entities.Job_Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Repository
@Transactional
public interface Job_RecruitmentRepository extends JpaRepository<Job_Recruitment, Long> {

    @Transactional
    @Modifying
    @Query("update Job_Recruitment j set j.id = ?2, j.from_date = ?3, j.job_description = ?4, j.status = ?5, " +
            "j.to_date = ?6, j.note = ?7, j.title = ?8 where j.id = ?1")
    void setJob_RecruitmentById(Long id,
                                 Long newId,
                                 LocalDate newFromDate,
                                 String newDescription,
                                 Integer newStatus,
                                 LocalDate newToDate,
                                 String newNote,
                                 String newTitle);

    @Transactional
    @Modifying
    @Query(value = "insert into Job_Recruitment (from_date, job_description, status, to_date, note, title) values (?1, ?2, ?3, ?4, ?5, ?6)", nativeQuery = true)
    void insertJob_RecruitmentById(LocalDate fromDate,
                                   String description,
                                   Integer status,
                                   LocalDate toDate,
                                   String note,
                                   String title);
}
