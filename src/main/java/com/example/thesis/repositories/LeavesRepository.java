package com.example.thesis.repositories;

import com.example.thesis.entities.Leaves;
import com.example.thesis.keys.LeavesPK;
import org.springframework.data.repository.CrudRepository;

public interface LeavesRepository extends CrudRepository<Leaves, LeavesPK> {
}
