package com.example.thesis.services;

import com.example.thesis.entities.Job_Recruitment;
import com.example.thesis.repositories.Job_RecruitmentRepository;
import com.example.thesis.responses.Job_RecruitmentResponse;
import com.google.common.collect.Lists;
import com.mysql.cj.exceptions.DataReadException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class JobService {
    private final Job_RecruitmentRepository repository;

    public List<Job_RecruitmentResponse> getJobs() {
        List<Job_RecruitmentResponse> jobsResponses = new ArrayList<>();

        Lists.newArrayList(repository.findAll()).forEach((Job_Recruitment job) -> {
            Job_RecruitmentResponse jobResponse = new Job_RecruitmentResponse(
                    job.getId(),
                    job.getTitle(),
                    job.getJob_description(),
                    job.getNote());

            jobsResponses.add(jobResponse);
        });

        return jobsResponses;
    }

    public Job_RecruitmentResponse getJobById(Long id) {
        Job_Recruitment job = repository.getById(id);
        Job_RecruitmentResponse jobResponse = new Job_RecruitmentResponse(
                job.getId(),
                job.getTitle(),
                job.getJob_description(),
                job.getNote());
        return jobResponse;
    }

    public void updateJob_RecruitmentById(Long id, Job_Recruitment job) {
        if (!repository.existsById(id)) {
            throw new DataReadException("There is no account with that id");
        }
        repository.setJob_RecruitmentById(
                id,
                job.getId(),
                job.getFrom_date(),
                job.getJob_description(),
                job.getStatus(),
                job.getTo_date(),
                job.getNote(),
                job.getTitle());
    }

    public void insertJobById(Long id, Job_Recruitment job) {
        repository.save(job);
    }
}
