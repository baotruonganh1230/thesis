package com.example.thesis.controllers;

import com.example.thesis.requests.UpdateLeaveRequest;
import com.example.thesis.responses.LeaveEmployeeList;
import com.example.thesis.services.LeaveService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/")
@AllArgsConstructor
public class LeaveController {
    private LeaveService leaveService;

    @GetMapping("leaves")
    public ResponseEntity<?> getLeaves(@RequestParam Long departmentId) {
        return new ResponseEntity<>(new LeaveEmployeeList(leaveService.getAllLeaves(departmentId)),
                HttpStatus.OK);
    }

    @PostMapping("leaves/status")
    public ResponseEntity<?> updateLeaveStatus(@RequestBody UpdateLeaveRequest updateLeaveRequest) {
        leaveService.updateLeavesStatusByIds(updateLeaveRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}