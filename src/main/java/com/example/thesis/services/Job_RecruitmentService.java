package com.example.thesis.services;

import com.example.thesis.entities.Job_Recruitment;
import com.example.thesis.repositories.DepartmentRepository;
import com.example.thesis.repositories.EmployeeRepository;
import com.example.thesis.repositories.Job_RecruitmentRepository;
import com.example.thesis.responses.VacanciesInfo;
import com.google.common.collect.Lists;
import com.mysql.cj.exceptions.DataReadException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class Job_RecruitmentService {
    private final Job_RecruitmentRepository repository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public List<VacanciesInfo> getJob_Recruitments() {
        List<VacanciesInfo> vacanciesInfos = new ArrayList<>();

        Lists.newArrayList(repository.findAll()).forEach((Job_Recruitment job) -> {
            VacanciesInfo vacanciesInfo = new VacanciesInfo(
                    job.getId(),
                    job.getEmployee().getId(),
                    job.getHas().getPosition().getId(),
                    job.getDepartment().getId(),
                    job.getPublished_date(),
                    job.getExpired_date(),
                    job.getQuantity(),
                    job.getStatus(),
                    job.getPostContent());

            vacanciesInfos.add(vacanciesInfo);
        });

        return vacanciesInfos;
    }

    public void updateJob_RecruitmentById(Long id, VacanciesInfo vacanciesInfo) {
        if (!repository.existsById(id)) {
            throw new DataReadException("There is no job_recruitment with that id");
        }
        repository.save(new Job_Recruitment(
                id,
                vacanciesInfo.getPostContent(),
                null,
                vacanciesInfo.getPublishedDate(),
                vacanciesInfo.getExpiredDate(),
                vacanciesInfo.getStatus(),
                vacanciesInfo.getQuantity(),
                employeeRepository.getById(vacanciesInfo.getHiringManagerId()),
                departmentRepository.getById(vacanciesInfo.getDepartmentId()),
                null,
                null));
    }
}
