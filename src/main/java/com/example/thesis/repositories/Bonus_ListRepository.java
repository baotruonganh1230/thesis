package com.example.thesis.repositories;

import com.example.thesis.entities.Bonus_List;
import com.example.thesis.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface Bonus_ListRepository extends JpaRepository<Bonus_List, Long> {

    List<Bonus_List> findAllByEmployee(Employee employee);

    @Transactional
    @Modifying
    @Query(value = "delete from bonus_list where id = ?1", nativeQuery = true)
    int deleteBonusById(Long id);

    @Transactional
    @Modifying
    @Query(value = "insert into bonus_list (amount, `name`, eid) values (?1, ?2, ?3)", nativeQuery = true)
    int insertNewBonus(BigDecimal amount, String name, Long eid);

    @Transactional
    @Modifying
    @Query(value = "update bonus_list set name = ?2, amount = ?3, eid = ?4 where id = ?1", nativeQuery = true)
    int updateBonus(Long id,String name,BigDecimal amount, Long eid);
}
