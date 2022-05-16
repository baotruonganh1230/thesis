package com.example.thesis.repositories;

import com.example.thesis.entities.Job_Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Repository
public interface Job_RecruitmentRepository extends JpaRepository<Job_Recruitment, Long> {
    @Transactional
    @Modifying
    @Query(value = "update job_recruitment set expired_date = ?2, note = ?3, post_content = ?4, published_date = ?5, quantity = ?6, status = ?7, department_id = ?8, hiring_manager_id = ?9 where id = ?1", nativeQuery = true)
    int updateJob_Recruitment(Long id, LocalDate expired_date, String note, String post_content, LocalDate published_date, Integer quantity, Integer status, Long department_id, Long hiring_manager_id);

    @Transactional
    @Modifying
    @Query(value = "delete from job_recruitment where department_id = ?1", nativeQuery = true)
    void deleteByDid(Long did);

    @Transactional
    @Modifying
    @Query(value = "update job_recruitment set status = ?2 where id = ?1", nativeQuery = true)
    void updateJobStatus(Long id, Integer status);
}
