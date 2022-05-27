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
    @Query(value = "update employee set date_of_birth = ?2, email = ?3, first_name = ?4, " +
            "avatar = ?5, last_name = ?6, permanent_address = ?7, phone = ?8, sex = ?9, " +
            "temporary_address = ?10 where id = ?1", nativeQuery = true)
    int setEmployeePersonalById(Long id, LocalDate date_of_birth, String email, String first_name,
                        String avatar, String last_name, String permanent_address,
                        String phone, String sex, String temporary_address);

    @Transactional
    @Modifying
    @Query(value = "update employee set employed_date = ?2, " +
            "gross_salary = ?3, " +
            "pit = ?4, shift_id = ?5, position_id = ?6 where id = ?1", nativeQuery = true)
    int setEmployeeJobById(Long id, LocalDate employed_date,
                        BigDecimal gross_salary, String pit, Long shiftId, Long position_id);


}
