package com.example.thesis.controllers;

import com.example.thesis.requests.ShiftRequest;
import com.example.thesis.services.ShiftService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/")
@AllArgsConstructor
public class ShiftController {
    private final ShiftService shiftService;

    @GetMapping("shifts")
    public ResponseEntity<?> getShifts() {
        return new ResponseEntity<>(shiftService.getShifts(), HttpStatus.OK);
    }

    @GetMapping("shift/{id}")
    public ResponseEntity<?> getShift(@PathVariable Long id) {
        return new ResponseEntity<>(shiftService.getShiftById(id), HttpStatus.OK);
    }

    @PutMapping("shift/{id}")
    public ResponseEntity<?> updateShift(@PathVariable Long id,
                                       @RequestBody ShiftRequest shiftRequest) {
        shiftService.updateShiftById(id, shiftRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("shift")
    public ResponseEntity<?> insertShift(@RequestBody ShiftRequest shiftRequest) {
        shiftService.insertShift(shiftRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
