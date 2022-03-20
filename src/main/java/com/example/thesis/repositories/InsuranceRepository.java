package com.example.thesis.repositories;

import com.example.thesis.entities.Insurance;
import com.example.thesis.keys.InsurancePK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsuranceRepository extends JpaRepository<Insurance, InsurancePK> {
    Insurance getById(Long id);
}
