package com.example.thesis.controllers;

import com.example.thesis.responses.Candidates;
import com.example.thesis.services.CandidateService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/")
@AllArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;

    @GetMapping("candidates")
    public ResponseEntity<?> getCandidates(@RequestParam Long jobRecruitmentId) {
        return new ResponseEntity<>(
                new Candidates(candidateService.getCandidatesByJob_RecruitmentId(jobRecruitmentId)),
                HttpStatus.OK);
    }
}