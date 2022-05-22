package com.example.thesis.repositories;

import com.example.thesis.entities.Candidate;
import com.example.thesis.entities.Job_Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {

    @Query(value = "select * from candidate c where c.job_recruitment_id = ?1", nativeQuery = true)
    List<Candidate> findAllByJob_RecruitmentId(Long job_recruitment_id);

    boolean existsByNameAndJobRecruitment(String name, Job_Recruitment jobRecruitment);

    long countByJobRecruitment(Job_Recruitment job_recruitment);
}
