package com.example.thesis.controllers;

import com.example.thesis.requests.LeaveRequest;
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
    public ResponseEntity<?> getLeaves(@RequestParam(required=false) Long departmentId) {
        return new ResponseEntity<>(new LeaveEmployeeList(leaveService.getAllLeaves(departmentId)),
                HttpStatus.OK);
    }

    @PostMapping("leaves/status")
    public ResponseEntity<?> updateLeaveStatus(@RequestBody UpdateLeaveRequest updateLeaveRequest) {
        leaveService.updateLeavesStatusByIds(updateLeaveRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("sendRequestLeave")
    public ResponseEntity<?> sendRequestLeave(@RequestBody LeaveRequest leaveRequest) {
        leaveService.insertRequestLeave(leaveRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("getLeave")
    public ResponseEntity<?> getLeave(@RequestParam Long userId) {
        return new ResponseEntity<>(leaveService.getLeave(userId), HttpStatus.OK);
    }
}
