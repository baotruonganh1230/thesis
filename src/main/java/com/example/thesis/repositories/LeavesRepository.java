package com.example.thesis.repositories;

import com.example.thesis.entities.Leaves;
import com.example.thesis.keys.LeavesPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeavesRepository extends JpaRepository<Leaves, LeavesPK> {
    Leaves getById(Long id);
}
