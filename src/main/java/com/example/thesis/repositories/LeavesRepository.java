package com.example.thesis.repositories;

import com.example.thesis.entities.Employee;
import com.example.thesis.entities.Leaves;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeavesRepository extends JpaRepository<Leaves, Long> {
    @Query(value = "select * from leaves where id = ?1", nativeQuery = true)
    Leaves getLeaveById(Long id);
    List<Leaves> getAllByEmployee(Employee employee);
    List<Leaves> findAllByApplicationDate(LocalDate applicationDate);

    @Query(value = "select * from leaves where eid = ?1 AND (from_date <= ?3 AND to_date >= ?2)", nativeQuery = true)
    List<Leaves> findAllByEmployeeAndPeriod(Long eid, LocalDate fromDate, LocalDate toDate);

    @Query(value = "select * from leaves where eid = ?1 AND (from_date <= ?2 AND to_date >= ?2) limit 1", nativeQuery = true)
    Leaves findByEmployeeAndDate(Long eid, LocalDate date);

//    @Query(value = "select * from leaves where id = ?1", nativeQuery = true)
//    List<Leaves> testSelectLeaves(Long id);
}
