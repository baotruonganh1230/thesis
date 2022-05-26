package com.example.thesis.services;

import com.example.thesis.entities.Job_Recruitment;
import com.example.thesis.entities.Position;
import com.example.thesis.repositories.*;
import com.example.thesis.responses.UnauthorizedJob_RecruitmentResponse;
import com.example.thesis.responses.VacanciesInfo;
import com.example.thesis.responses.VacanciesInfos;
import com.google.common.collect.Lists;
import com.mysql.cj.exceptions.DataReadException;
import lombok.AllArgsConstructor;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class Job_RecruitmentService {
    private final Job_RecruitmentRepository repository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final CandidateRepository candidateRepository;
    private final HasRepository hasRepository;
    private final PositionRepository positionRepository;

    public List<UnauthorizedJob_RecruitmentResponse> getJob_RecruitmentsUnauthorized() {

        List<UnauthorizedJob_RecruitmentResponse> unauthorizedJob_recruitmentResponses
                = new ArrayList<>();

        repository.findAll()
                .forEach((Job_Recruitment job) -> {
                    Integer newStatus;
                    long candidateCount = candidateRepository.countByJobRecruitment(job);

                    if (job.getExpired_date().isBefore(LocalDate.now())) {
                        newStatus = 1;
                    } else if (candidateCount >= job.getQuantity()) {
                        newStatus = 2;
                    } else {
                        newStatus = 0;
                    }

                    if (!newStatus.equals(job.getStatus())) {
                        repository.updateJobStatus(job.getId(), newStatus);
                    }

                    if (newStatus == 0) {
                        UnauthorizedJob_RecruitmentResponse unauthorizedJob_recruitmentResponse =
                                new UnauthorizedJob_RecruitmentResponse(
                                        job.getId(),
                                        job.getHas().getPosition().getName(),
                                        job.getDepartment().getName(),
                                        job.getPostContent());

                        unauthorizedJob_recruitmentResponses.add(unauthorizedJob_recruitmentResponse);
                    }
                });

        return unauthorizedJob_recruitmentResponses;
    }

    public VacanciesInfos getJob_Recruitments(Optional<Integer> page, Optional<String> sortBy, Optional<String> sortOrder) {
        List<VacanciesInfo> vacanciesInfos = new ArrayList<>();

        Lists.newArrayList(repository.findAll()).forEach((Job_Recruitment job) -> {
            Integer newStatus;
            long candidateCount = candidateRepository.countByJobRecruitment(job);

            if (job.getExpired_date().isBefore(LocalDate.now())) {
                newStatus = 1;
            } else if (candidateCount >= job.getQuantity()) {
                newStatus = 2;
            } else {
                newStatus = 0;
            }

            if (!newStatus.equals(job.getStatus())) {
                repository.updateJobStatus(job.getId(), newStatus);
            }

            VacanciesInfo vacanciesInfo = new VacanciesInfo(
                    job.getId(),
                    job.getEmployee().getId(),
                    job.getHas().getPosition().getId(),
                    job.getDepartment().getId(),
                    job.getPublished_date(),
                    job.getExpired_date(),
                    job.getQuantity(),
                    newStatus,
                    job.getPostContent());

            vacanciesInfos.add(vacanciesInfo);
        });

        PagedListHolder<VacanciesInfo> pages = new PagedListHolder<>(
                vacanciesInfos,
                new MutableSortDefinition(
                        sortBy.orElse("id"),
                        true,
                        sortOrder.orElse("asc").equalsIgnoreCase("asc")
                ));
        pages.resort();
        pages.setPage(page.orElse(0)); //set current page number
        pages.setPageSize(10); // set the size of page

        List<VacanciesInfo> pageList = pages.getPageList();

        return new VacanciesInfos(
                page.orElse(0) >= pages.getPageCount() ? new ArrayList<>() : pageList,
                page.orElse(0).equals(pages.getPageCount() - 1),
                pages.getPageCount(),
                pages.getNrOfElements(),
                pages.getPageSize(),
                page.orElse(0),
                page.orElse(0).equals(0),
                page.orElse(0) >= pages.getPageCount() ? 0 : pages.getPageList().size(),
                pages.getPageList().size() == 0
        );
    }

    public void updateJob_RecruitmentById(Long id, VacanciesInfo vacanciesInfo) {
        if (!repository.existsById(id)) {
            throw new DataReadException("There is no job_recruitment with that id");
        }

        int status;

        if (LocalDate.parse(vacanciesInfo.getExpiredDate()).isBefore(LocalDate.now())) {
            status = 1;
        } else if (vacanciesInfo.getQuantity() <= 0) {
            status = 2;
        } else {
            status = 0;
        }

        repository.updateJob_Recruitment(
                id,
                LocalDate.parse(vacanciesInfo.getExpiredDate()),
                null,
                vacanciesInfo.getPostContent(),
                LocalDate.parse(vacanciesInfo.getPublishedDate()),
                vacanciesInfo.getQuantity(),
                status,
                vacanciesInfo.getDepartmentId(),
                vacanciesInfo.getHiringManagerId()
        );

        Position position = positionRepository.getById(vacanciesInfo.getPositionId());

        if (hasRepository.existsByPosition(position)) {
            hasRepository.updateJob_RecuitmentIdByPosId(vacanciesInfo.getPositionId(), id);
        } else {
            hasRepository.insertHas(vacanciesInfo.getPositionId(), id);
        }
    }

    public void insertJob_Recruitment(VacanciesInfo vacanciesInfo) {
        int status;

        if (LocalDate.parse(vacanciesInfo.getExpiredDate()).isBefore(LocalDate.now())) {
            status = 1;
        } else if (vacanciesInfo.getQuantity() <= 0) {
            status = 2;
        } else {
            status = 0;
        }

        Job_Recruitment savedJob_recruitment = repository.save(new Job_Recruitment(
                null,
                vacanciesInfo.getPostContent(),
                null,
                LocalDate.parse(vacanciesInfo.getPublishedDate()),
                LocalDate.parse(vacanciesInfo.getExpiredDate()),
                status,
                vacanciesInfo.getQuantity(),
                employeeRepository.getById(vacanciesInfo.getHiringManagerId()),
                departmentRepository.getById(vacanciesInfo.getDepartmentId()),
                null,
                null));

        Position position = positionRepository.getById(vacanciesInfo.getPositionId());

        if (hasRepository.existsByPosition(position)) {
            hasRepository.updateJob_RecuitmentIdByPosId(vacanciesInfo.getPositionId(), savedJob_recruitment.getId());
        } else {
            hasRepository.insertHas(vacanciesInfo.getPositionId(), savedJob_recruitment.getId());
        }
    }
}
