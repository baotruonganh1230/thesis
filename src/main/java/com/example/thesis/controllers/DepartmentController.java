package com.example.thesis.controllers;

import com.example.thesis.repositories.DepartmentRepository;
import com.example.thesis.requests.DepartmentRequest;
import com.example.thesis.services.DepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/")
@AllArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;
    private final DepartmentRepository repository;

    @GetMapping("departments")
    public ResponseEntity<?> getDepartments(@RequestParam Boolean nested) {
        return new ResponseEntity<>(departmentService.getDepartments(nested), HttpStatus.OK);
    }

    @GetMapping("department/{id}")
    public ResponseEntity<?> getDepartment(@PathVariable Long id) {
        return new ResponseEntity<>(departmentService.getDepartment(id), HttpStatus.OK);
    }

    @PutMapping("department/{id}")
    public ResponseEntity<?> updateDepartment(@PathVariable Long id,
                                           @RequestBody DepartmentRequest departmentRequest) {
        departmentService.updateDepartmentById(id, departmentRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("department")
    public ResponseEntity<?> insertDepartment(@RequestBody DepartmentRequest departmentRequest) {
        departmentService.insertDepartmentById(departmentRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
