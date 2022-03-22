package com.example.thesis.repositories;

import com.example.thesis.entities.Manage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ManageRepository extends JpaRepository<Manage, Long> {
    @Transactional
    @Modifying
    @Query(value = "update manage set eid = ?2 where did = ?1", nativeQuery = true)
    int setEmployeeId(Long did, Long eid);
}
