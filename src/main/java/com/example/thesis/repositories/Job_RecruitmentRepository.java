package com.example.thesis.repositories;

import com.example.thesis.entities.Job_Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface Job_RecruitmentRepository extends JpaRepository<Job_Recruitment, Long> {
}
