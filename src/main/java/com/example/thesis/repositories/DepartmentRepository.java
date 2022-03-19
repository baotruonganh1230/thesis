package com.example.thesis.repositories;

import com.example.thesis.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    List<Department> findAllByHeadOfUnit(Department headOfUnit);

    @Transactional
    @Modifying
    @Query(value = "update Department d set d.head_of_unit_id = ?2 where d.id = ?1", nativeQuery = true)
    int setHeadOfUnit(Long id, Long headOfUnitId);

    @Transactional
    @Modifying
    @Query(value = "update Department d set d.location = ?2, d.type = ?3, d.name = ?4, d.people_number = ?5, d.description = ?6, d.head_of_unit_id = ?7 where d.id = ?1", nativeQuery = true)
    int setDepartmentById(Long id,String location,String type,String name,Integer peopleNumber,String description,Long headOfUnitId);

    @Transactional
    @Modifying
    @Query(value = "insert into Department (location, type, name, people_number, description, head_of_unit_id) values (?1, ?2, ?3, ?4, ?5, ?6)", nativeQuery = true)
    int insertDepartmentById(String location,String type,String name,Integer peopleNumber,String description,Long headOfUnitId);
}
