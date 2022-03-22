package com.example.thesis.repositories;

import com.example.thesis.entities.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {

    @Query(value = "select * from candidate c where c.job_recruitment_id = ?1", nativeQuery = true)
    List<Candidate> findAllByJob_RecruitmentId(Long job_recruitment_id);
}
