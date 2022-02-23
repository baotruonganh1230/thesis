package com.example.thesis.service;

import com.example.thesis.entity.Employee;
import com.example.thesis.repositories.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Transactional
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }
}
