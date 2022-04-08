package com.example.thesis.services;

import com.example.thesis.entities.Job_Recruitment;
import com.example.thesis.repositories.DepartmentRepository;
import com.example.thesis.repositories.EmployeeRepository;
import com.example.thesis.repositories.HasRepository;
import com.example.thesis.repositories.Job_RecruitmentRepository;
import com.example.thesis.responses.UnauthorizedJob_RecruitmentResponse;
import com.example.thesis.responses.VacanciesInfo;
import com.google.common.collect.Lists;
import com.mysql.cj.exceptions.DataReadException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class Job_RecruitmentService {
    private final Job_RecruitmentRepository repository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final HasRepository hasRepository;

    public List<UnauthorizedJob_RecruitmentResponse> getJob_RecruitmentsUnauthorized() {
        List<UnauthorizedJob_RecruitmentResponse> unauthorizedJob_recruitmentResponses
                = new ArrayList<>();

        Lists.newArrayList(repository.findAll()).forEach((Job_Recruitment job) -> {
            UnauthorizedJob_RecruitmentResponse unauthorizedJob_recruitmentResponse =
                    new UnauthorizedJob_RecruitmentResponse(
                    job.getId(),
                    job.getHas().getPosition().getName(),
                    job.getDepartment().getName(),
                    job.getPostContent());

            unauthorizedJob_recruitmentResponses.add(unauthorizedJob_recruitmentResponse);
        });

        return unauthorizedJob_recruitmentResponses;
    }

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
        repository.updateJob_Recruitment(
                id,
                LocalDate.parse(vacanciesInfo.getExpiredDate()),
                null,
                vacanciesInfo.getPostContent(),
                LocalDate.parse(vacanciesInfo.getPublishedDate()),
                vacanciesInfo.getQuantity(),
                vacanciesInfo.getStatus(),
                vacanciesInfo.getDepartmentId(),
                vacanciesInfo.getHiringManagerId()
        );

        if (hasRepository.existsByPosid(vacanciesInfo.getPositionId())) {
            hasRepository.updateJob_RecuitmentIdByPosId(vacanciesInfo.getPositionId(), id);
        } else {
            hasRepository.insertHas(vacanciesInfo.getPositionId(), id);
        }
    }

    public void insertJob_Recruitment(VacanciesInfo vacanciesInfo) {
        Job_Recruitment savedJob_recruitment = repository.save(new Job_Recruitment(
                null,
                vacanciesInfo.getPostContent(),
                null,
                LocalDate.parse(vacanciesInfo.getPublishedDate()),
                LocalDate.parse(vacanciesInfo.getExpiredDate()),
                vacanciesInfo.getStatus(),
                vacanciesInfo.getQuantity(),
                employeeRepository.getById(vacanciesInfo.getHiringManagerId()),
                departmentRepository.getById(vacanciesInfo.getDepartmentId()),
                null,
                null));

        if (hasRepository.existsByPosid(vacanciesInfo.getPositionId())) {
            hasRepository.updateJob_RecuitmentIdByPosId(vacanciesInfo.getPositionId(), savedJob_recruitment.getId());
        } else {
            hasRepository.insertHas(vacanciesInfo.getPositionId(), savedJob_recruitment.getId());
        }
    }
}
