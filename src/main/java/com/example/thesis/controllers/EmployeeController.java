package com.example.thesis.controllers;

import com.example.thesis.repositories.Insurance_TypeRepository;
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

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("api/v1/")
@AllArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;
    private final GoogleDriveService googleDriveService;
    private final Insurance_TypeRepository insurance_typeRepository;

    @GetMapping("employees")
    public ResponseEntity<?> getEmployees() {
        return new ResponseEntity<>(employeeService.getEmployees(), HttpStatus.OK);
    }

    @GetMapping("employee/{id}")
    public ResponseEntity<?> getEmployee(@PathVariable Long id) {
        return new ResponseEntity<>(employeeService.getEmployee(id), HttpStatus.OK);
    }

    @PutMapping("employee/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable Long id,
                                              @RequestParam("avatar") MultipartFile file,
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

    @PostMapping("employee/{id}")
    public ResponseEntity<?> insertEmployee(@PathVariable Long id,
                                            @RequestParam("avatar") MultipartFile file,
                                            @RequestParam("data") String employeeRequestString) {
        ObjectMapper mapper = new ObjectMapper();
        EmployeeRequest employeeRequest = null;
        try {
            employeeRequest = mapper.readValue(employeeRequestString, EmployeeRequest.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        employeeService.insertEmployeeById(id, file, employeeRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("upload")
    public String uploadFile() throws IOException {
//        googleDriveService.uploadFile();
        File originalFile = new File("C:\\Users\\baoatruong\\Downloads\\2925.png");
        com.google.api.services.drive.model.File uploadedFile = googleDriveService.upLoadFile(originalFile.getName(), originalFile.getAbsolutePath(), "image/png");
        return uploadedFile.toPrettyString();
//        return new ResponseEntity<>(employeeService.getEmployees(), HttpStatus.OK);
    }
//
//    @GetMapping("employee/{id}")
//    public ResponseEntity<?> getEmployee(@PathVariable Long id) {
//        return new ResponseEntity<>(employeeService.getEmployee(id), HttpStatus.OK);
//    }
}
