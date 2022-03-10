package com.example.thesis.repositories;

import com.example.thesis.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Transactional
    @Modifying
    @Query(value = "update Employee e set e.date_of_birth = ?2, e.email = ?3, e.first_name = ?4, " +
            "e.avatar = ?5, e.last_name = ?6, e.permanent_address = ?7, e.phone = ?8, e.sex = ?9, " +
            "e.temporary_address = ?10 where e.id = ?1", nativeQuery = true)
    int setEmployeePersonalById(Long id, LocalDate date_of_birth, String email, String first_name,
                        String avatar, String last_name, String permanent_address,
                        String phone, String sex, String temporary_address);

    @Transactional
    @Modifying
    @Query(value = "update Employee e set e.employed_date = ?2, " +
            "e.gross_salary = ?3, " +
            "e.pit = ?4, e.position_id = ?5 where e.id = ?1", nativeQuery = true)
    int setEmployeeJobById(Long id, LocalDate employed_date,
                        BigDecimal gross_salary, String pit, Long position_id);

    @Transactional
    @Modifying
    @Query(value = "insert into Employee e (id,\n" +
            "date_of_birth,\n" +
            "email,\n" +
            "employed_date,\n" +
            "first_name,\n" +
            "gross_salary,\n" +
            "avatar,\n" +
            "last_name,\n" +
            "permanent_address,\n" +
            "phone,\n" +
            "sex,\n" +
            "temporary_address,\n" +
            "pit,\n" +
            "position_id) values (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11, ?12, ?13, ?14)", nativeQuery = true)
    int insertEmployeeById(Long id, LocalDate date_of_birth, String email, LocalDate employed_date, String first_name,
                           BigDecimal gross_salary, String avatar, String last_name, String permanent_address,
                           String phone, String sex, String temporary_address, String pit, Long position_id);


}
