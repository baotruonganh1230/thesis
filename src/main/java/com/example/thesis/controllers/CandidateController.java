package com.example.thesis.controllers;

import com.example.thesis.services.CandidateService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/")
@AllArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;

    @GetMapping("candidates")
    public ResponseEntity<?> getCandidates(@RequestParam(required=false) Long jobRecruitmentId,
                                           @RequestParam Optional<Integer> pagination,
                                           @RequestParam Optional<String> sortBy,
                                           @RequestParam Optional<String> sortOrder) {
        return new ResponseEntity<>(
                candidateService.getCandidatesByJob_RecruitmentId(jobRecruitmentId, pagination, sortBy, sortOrder),
                HttpStatus.OK);
    }

    @PostMapping("promote/{candidateId}")
    public ResponseEntity<?> promoteCandidate(@PathVariable Long candidateId) {
        candidateService.promoteCandidate(candidateId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("reject/{candidateId}")
    public ResponseEntity<?> rejectCandidate(@PathVariable Long candidateId) {
        candidateService.rejectCandidate(candidateId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
