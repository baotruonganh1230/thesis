package com.example.thesis.repositories;

import com.example.thesis.entities.Employee;
import com.example.thesis.entities.Leaves;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeavesRepository extends JpaRepository<Leaves, Long> {
    Leaves getById(Long id);
    List<Leaves> getAllByEmployee(Employee employee);
    List<Leaves> findAllByApplicationDate(LocalDate applicationDate);
}
