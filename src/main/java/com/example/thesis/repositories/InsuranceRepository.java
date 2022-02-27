package com.example.thesis.repositories;

import com.example.thesis.entities.Insurance;
import com.example.thesis.keys.InsurancePK;
import org.springframework.data.repository.CrudRepository;

public interface InsuranceRepository extends CrudRepository<Insurance, InsurancePK> {
}
