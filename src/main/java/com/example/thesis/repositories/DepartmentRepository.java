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
    @Query(value = "update department set head_of_unit_id = ?2 where id = ?1", nativeQuery = true)
    int setHeadOfUnit(Long id, Long headOfUnitId);

    @Transactional
    @Modifying
    @Query(value = "update department set people_number = ?2 where id = ?1", nativeQuery = true)
    int setPeopleCount(Long id, Integer peopleNumber);

    @Transactional
    @Modifying
    @Query(value = "update department set people_number = people_number + 1 where id = ?1", nativeQuery = true)
    int increasePeopleCount(Long id);

    @Transactional
    @Modifying
    @Query(value = "update department set people_number = people_number - 1 where id = ?1", nativeQuery = true)
    int decreasePeopleCount(Long id);

    @Transactional
    @Modifying
    @Query(value = "update department set name = ?2, description = ?3 where id = ?1", nativeQuery = true)
    int setDepartmentById(Long id,String name,String description);

    @Transactional
    @Modifying
    @Query(value = "insert into department (name, description, head_of_unit_id) values (?1, ?2, ?3)", nativeQuery = true)
    int insertDepartmentById(String name,String description,Long headOfUnitId);

    @Transactional
    @Modifying
    @Query(value = "delete from department where id = ?1", nativeQuery = true)
    void deleteById(Long id);

    Department findByName(String name);
}
