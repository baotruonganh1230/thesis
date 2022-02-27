package com.example.thesis.services;

import com.example.thesis.entities.Employee;
import com.example.thesis.repositories.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Long count() {
        return employeeRepository.count();
    }
}
