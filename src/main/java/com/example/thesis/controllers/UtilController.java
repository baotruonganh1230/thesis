package com.example.thesis.controllers;

import com.example.thesis.services.UtilService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/")
@AllArgsConstructor
public class UtilController {
    private final UtilService utilService;

    @GetMapping("chart/attendHour")
    public ResponseEntity<?> getAverageWorkingHours(@RequestParam(required=false) String week) {
        return new ResponseEntity<>(utilService.getAverageWorkingHoursByWeek(week), HttpStatus.OK);
    }

    @GetMapping("chart/attendNumber")
    public ResponseEntity<?> getEmployeeAttendStatus(@RequestParam(required=false) String week) {
        return new ResponseEntity<>(utilService.getEmployeeAttendStatus(week), HttpStatus.OK);
    }

    @GetMapping("link")
    public ResponseEntity<?> getPayrollPdf(@RequestParam(required=false) String month) {
        return new ResponseEntity<>(utilService.getPayrollPdf(month), HttpStatus.OK);
    }
}
