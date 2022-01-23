package com.example.thesis.repositories;

import com.example.thesis.entity.Leaves;
import com.example.thesis.key.LeavesPK;
import org.springframework.data.repository.CrudRepository;

public interface LeavesRepository extends CrudRepository<Leaves, LeavesPK> {
}
