package com.example.thesis.controllers;

import com.example.thesis.requests.EmployeeRequest;
import com.example.thesis.services.EmployeeService;
import com.example.thesis.services.GoogleDriveService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/")
@AllArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;
    private final GoogleDriveService googleDriveService;

    @GetMapping("employees")
    public ResponseEntity<?> getEmployees(@RequestParam(required=false) Boolean isHavingDepartment) {
        return new ResponseEntity<>(employeeService.getEmployees(isHavingDepartment), HttpStatus.OK);
    }

    @GetMapping("employee/{id}")
    public ResponseEntity<?> getEmployee(@PathVariable Long id) {
        return new ResponseEntity<>(employeeService.getEmployee(id), HttpStatus.OK);
    }

    @PutMapping("employee/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable Long id,
                                              @RequestParam(value = "avatar", required = false) MultipartFile file,
                                            @RequestParam("data") String employeeRequestString) {
        ObjectMapper mapper = new ObjectMapper();
        EmployeeRequest employeeRequest = null;
        try {
            employeeRequest = mapper.readValue(employeeRequestString, EmployeeRequest.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        employeeService.updateEmployeeById(id, file, employeeRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("employee")
    public ResponseEntity<?> insertEmployee(@RequestParam("avatar") MultipartFile file,
                                            @RequestParam("data") String employeeRequestString) {
        ObjectMapper mapper = new ObjectMapper();
        EmployeeRequest employeeRequest = null;
        try {
            employeeRequest = mapper.readValue(employeeRequestString, EmployeeRequest.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        employeeService.insertEmployeeById(file, employeeRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
