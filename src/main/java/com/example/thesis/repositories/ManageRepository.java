package com.example.thesis.repositories;

import com.example.thesis.entities.Employee;
import com.example.thesis.entities.Manage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ManageRepository extends JpaRepository<Manage, Long> {

    @Query(value = "select * from manage where did = ?1 limit 1", nativeQuery = true)
    Manage getManageByDid(Long did);

    @Query(value = "select * from manage where eid = ?1 limit 1", nativeQuery = true)
    Manage getManageByEid(Long eid);

    int deleteByEmployee(Employee employee);

    boolean existsByEmployee(Employee employee);

    @Transactional
    @Modifying
    @Query(value = "update manage set eid = ?2 where did = ?1", nativeQuery = true)
    int setEmployeeId(Long did, Long eid);

    @Transactional
    @Modifying
    @Query(value = "delete from manage where did = ?1", nativeQuery = true)
    void deleteByDid(Long did);
}
