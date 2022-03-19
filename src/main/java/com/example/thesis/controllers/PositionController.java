package com.example.thesis.controllers;

import com.example.thesis.entities.Position;
import com.example.thesis.services.PositionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/")
@AllArgsConstructor
public class PositionController {
    private final PositionService positionService;

    @GetMapping("positions")
    public ResponseEntity<?> getJobs() {
        return new ResponseEntity<>(positionService.getPostions(), HttpStatus.OK);
    }

    @GetMapping("position/{id}")
    public ResponseEntity<?> getJob(@PathVariable Long id) {
        return new ResponseEntity<>(positionService.getPostionById(id), HttpStatus.OK);
    }

    @PutMapping("position/{id}")
    public ResponseEntity<?> updateJob(@PathVariable Long id,
                                       @RequestBody Position position) {
        positionService.updatePositionById(id, position);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("position/{id}")
    public ResponseEntity<?> insertJob(@PathVariable Long id,
                                       @RequestBody Position position) {
        positionService.insertPositionById(id, position);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
