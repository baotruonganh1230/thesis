package com.example.thesis.repositories;

import com.example.thesis.entities.Bonus_List;
import com.example.thesis.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface Bonus_ListRepository extends JpaRepository<Bonus_List, Long> {

    List<Bonus_List> findAllByEmployee(Employee employee);

    @Transactional
    @Modifying
    @Query(value = "delete from bonus_list where id = ?1", nativeQuery = true)
    int deleteBonusById(Long id);
}
