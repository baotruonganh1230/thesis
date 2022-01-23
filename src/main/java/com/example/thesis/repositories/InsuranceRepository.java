package com.example.thesis.repositories;

import com.example.thesis.entity.Insurance;
import com.example.thesis.key.InsurancePK;
import org.springframework.data.repository.CrudRepository;

public interface InsuranceRepository extends CrudRepository<Insurance, InsurancePK> {
}
