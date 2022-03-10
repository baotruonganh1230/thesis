package com.example.thesis.repositories;

import com.example.thesis.entities.Insurance_Type;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Insurance_TypeRepository extends JpaRepository<Insurance_Type, Long> {
    Insurance_Type findByName(String name);
}
