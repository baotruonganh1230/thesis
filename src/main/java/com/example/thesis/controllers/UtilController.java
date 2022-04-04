package com.example.thesis.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/")
@AllArgsConstructor
public class UtilController {

    @GetMapping("chart/attendHour")
    public ResponseEntity<?> getAverageWorkingHours(@PathVariable String week) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("chart/attendNumber")
    public ResponseEntity<?> getEmployeeStatus(@PathVariable String week) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
