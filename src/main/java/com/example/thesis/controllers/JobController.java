package com.example.thesis.controllers;


import com.example.thesis.entities.Job_Recruitment;
import com.example.thesis.services.JobService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/")
@AllArgsConstructor
public class JobController {
    private final JobService jobService;

    @GetMapping("jobs")
    public ResponseEntity<?> getJobs() {
        return new ResponseEntity<>(jobService.getJobs(), HttpStatus.OK);
    }

    @GetMapping("job/{id}")
    public ResponseEntity<?> getJob(@PathVariable Long id) {
        return new ResponseEntity<>(jobService.getJobById(id), HttpStatus.OK);
    }

    @PutMapping("job/{id}")
    public ResponseEntity<?> updateJob(@PathVariable Long id,
                                       @RequestBody Job_Recruitment job_recruitment) {
        jobService.updateJob_RecruitmentById(id, job_recruitment);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("job/{id}")
    public ResponseEntity<?> insertJob(@PathVariable Long id,
                                       @RequestBody Job_Recruitment job_recruitment) {
        jobService.insertJobById(id, job_recruitment);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
