package com.example.thesis.controllers;


import com.example.thesis.responses.VacanciesInfo;
import com.example.thesis.services.Job_RecruitmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/")
@AllArgsConstructor
public class Job_RecruitmentController {
    private final Job_RecruitmentService jobRecruitmentService;

    @GetMapping("job_recruitments")
    public ResponseEntity<?> getJob_Recruitments() {
        return new ResponseEntity<>(jobRecruitmentService.getJob_Recruitments(), HttpStatus.OK);
    }

    @PutMapping("job_recruitment/{id}")
    public ResponseEntity<?> updateJob(@PathVariable Long id,
                                       @RequestBody VacanciesInfo vacanciesInfo) {
        jobRecruitmentService.updateJob_RecruitmentById(id, vacanciesInfo);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
