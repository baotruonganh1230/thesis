package com.example.thesis.controllers;

import com.example.thesis.requests.CandidateRequest;
import com.example.thesis.services.CandidateService;
import com.example.thesis.services.Job_RecruitmentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/unauthorized/")
@AllArgsConstructor
public class UnauthorizedController {
    private final Job_RecruitmentService jobRecruitmentService;
    private final CandidateService candidateService;

    @GetMapping("job_recruitments")
    public ResponseEntity<?> getJob_RecruitmentsUnauthorized() {
        return new ResponseEntity<>(jobRecruitmentService.getJob_RecruitmentsUnauthorized(), HttpStatus.OK);
    }

    @PostMapping("candidate")
    public ResponseEntity<?> insertCandidateUnauthorized(@RequestParam("file") MultipartFile file,
                                                         @RequestParam("data") String candidateRequestString) {
        ObjectMapper mapper = new ObjectMapper();
        CandidateRequest candidateRequest = null;
        try {
            candidateRequest = mapper.readValue(candidateRequestString, CandidateRequest.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        candidateService.insertCandidateUnauthorized(file, candidateRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
