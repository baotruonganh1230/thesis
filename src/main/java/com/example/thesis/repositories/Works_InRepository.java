package com.example.thesis.repositories;

import com.example.thesis.entities.Works_In;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface Works_InRepository extends JpaRepository<Works_In, Long> {

    @Transactional
    @Modifying
    @Query(value = "update works_in set did = ?2 where eid = ?1", nativeQuery = true)
    int setDepartmentId(Long eid, Long did);

    @Query(value = "select count(*) as count from works_in where did = ?1", nativeQuery = true)
    long getCountByDid(Long did);

    @Transactional
    @Modifying
    @Query(value = "delete from works_in where did = ?1", nativeQuery = true)
    void deleteByDid(Long did);
}
