package com.example.thesis.controllers;

import com.example.thesis.services.AttendanceService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/")
@AllArgsConstructor
public class AttendanceController {
    private final AttendanceService attendanceService;

    @GetMapping("attendances")
    public ResponseEntity<?> getAttendances(@RequestParam(required = false) String week,
                                            @RequestParam(required = false) Long departmentId,
                                            @RequestParam Optional<Integer> pagination) {
        return new ResponseEntity<>(attendanceService.getAllAttendances(week, departmentId, pagination),
                HttpStatus.OK);
    }
}
