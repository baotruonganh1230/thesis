package com.example.thesis.repositories;

import com.example.thesis.entities.Leaves;
import com.example.thesis.keys.LeavesPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeavesRepository extends JpaRepository<Leaves, LeavesPK> {
    Leaves getById(Long id);
    List<Leaves> getAllByEid(Long eid);
}
