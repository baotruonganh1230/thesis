package com.example.thesis.repositories;

import com.example.thesis.entities.Has;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface HasRepository extends JpaRepository<Has, Long> {
    @Transactional
    @Modifying
    @Query(value = "update has set jrecruitid = ?2 where posid = ?1", nativeQuery = true)
    int updateJob_RecuitmentIdByPosId(Long posid, Long jrecruitid);

    @Transactional
    @Modifying
    @Query(value = "insert into has (posid, jrecruitid) values (?1, ?2)", nativeQuery = true)
    int insertHas(Long posid, Long jrecruitid);

    boolean existsByPosid(Long posId);
}
