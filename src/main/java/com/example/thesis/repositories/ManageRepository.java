package com.example.thesis.repositories;

import com.example.thesis.entities.Manage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManageRepository extends JpaRepository<Manage, Long> {
}
