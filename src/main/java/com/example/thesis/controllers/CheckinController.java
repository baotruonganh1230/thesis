package com.example.thesis.controllers;

import com.example.thesis.requests.CheckinRequest;
import com.example.thesis.services.CheckinService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/")
@AllArgsConstructor
public class CheckinController {
    private final CheckinService checkinService;

    @PostMapping("checkin_in")
    public ResponseEntity<?> checkin_in(@RequestBody CheckinRequest checkinRequest) {
        checkinService.checkin_in(checkinRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("checkin_out")
    public ResponseEntity<?> checkin_out(@RequestBody CheckinRequest checkinRequest) {
        checkinService.checkin_out(checkinRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("haveCheckedin")
    public ResponseEntity<?> haveCheckedin(@RequestParam Long userId, @RequestParam String date) {
        return new ResponseEntity<>(checkinService.haveCheckedin(userId, date), HttpStatus.OK);
    }
}
