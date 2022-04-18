package com.example.thesis.repositories;

import com.example.thesis.entities.Employee;
import com.example.thesis.entities.Leaves;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeavesRepository extends JpaRepository<Leaves, Long> {
    Leaves getById(Long id);
    List<Leaves> getAllByEmployee(Employee employee);
    Page<Leaves> findAllByApplicationDate(LocalDate applicationDate, Pageable pageable);

    @Query(value = "select * from leaves where eid = ?1 AND from_date <= ?2 AND to_date >= ?2", nativeQuery = true)
    List<Leaves> findAllByEmployeeAndDate(Long eid, LocalDate date);
}
