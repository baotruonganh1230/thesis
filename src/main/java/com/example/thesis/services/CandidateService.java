package com.example.thesis.services;

import com.example.thesis.entities.Candidate;
import com.example.thesis.entities.Job_Recruitment;
import com.example.thesis.repositories.CandidateRepository;
import com.example.thesis.repositories.Job_RecruitmentRepository;
import com.example.thesis.requests.CandidateRequest;
import com.example.thesis.requests.EmployeeRequest;
import com.example.thesis.requests.JobDetailInputParams;
import com.example.thesis.requests.PersonalDetailInputParams;
import com.example.thesis.responses.CandidateResponse;
import com.example.thesis.responses.Candidates;
import lombok.AllArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CandidateService {
    private final CandidateRepository candidateRepository;
    private final EmployeeService employeeService;
    private final GoogleDriveService googleDriveService;
    private final Job_RecruitmentRepository job_recruitmentRepository;

    public Candidates getCandidatesByJob_RecruitmentId(Long job_recruitmentId, Optional<Integer> page, Optional<String> sortBy, Optional<String> sortOrder) {
        List<CandidateResponse> candidateList;
        if (job_recruitmentId != null) {
            List<Candidate> candidates = candidateRepository.findAllByJob_RecruitmentId(job_recruitmentId);
            candidateList = candidates.stream().map(candidate ->
                            new CandidateResponse(candidate.getId(),
                                    candidate.getJobRecruitment().getId(),
                                    candidate.getName(),
                                    candidate.getJobRecruitment().getHas().getPosition().getName(),
                                    candidate.getJobRecruitment().getDepartment().getName(),
                                    candidate.getAppliedDate(),
                                    candidate.getCv_file(),
                                    candidate.getEmail(),
                                    candidate.getContact()))
                    .collect(Collectors.toList());


        } else {
            candidateList = candidateRepository.findAll().stream().map(candidate ->
                            new CandidateResponse(candidate.getId(),
                                    candidate.getJobRecruitment().getId(),
                                    candidate.getName(),
                                    candidate.getJobRecruitment().getHas().getPosition().getName(),
                                    candidate.getJobRecruitment().getDepartment().getName(),
                                    candidate.getAppliedDate(),
                                    candidate.getCv_file(),
                                    candidate.getEmail(),
                                    candidate.getContact()))
                    .collect(Collectors.toList());
        }

        PagedListHolder<CandidateResponse> pages = new PagedListHolder<>(
                candidateList,
                new MutableSortDefinition(
                        sortBy.orElse("id"),
                        true,
                        sortOrder.orElse("asc").equalsIgnoreCase("asc")
                ));
        pages.resort();
        pages.setPage(page.orElse(0)); //set current page number
        pages.setPageSize(10); // set the size of page

        List<CandidateResponse> pageList = pages.getPageList();

        return new Candidates(
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

    @Transactional
    public void insertCandidateUnauthorized(MultipartFile file, CandidateRequest candidateRequest) {
        File filetoUpload = employeeService.convertMultiPartFiletoFile(file);
        Tika tika = new Tika();
        String mimeType = null;
        try {
            mimeType = tika.detect(filetoUpload);
        } catch (IOException e) {
            e.printStackTrace();
        }
        com.google.api.services.drive.model.File upLoadedFile =
                googleDriveService.upLoadFile(filetoUpload.getName(),
                        filetoUpload.getAbsolutePath(), mimeType);

        if (!filetoUpload.delete()) {
            throw new IllegalStateException("Cannot delete file!");
        }
        Job_Recruitment job_recruitment = job_recruitmentRepository.getById(candidateRequest.getJobRecruitmentId());

        if (!candidateRepository.existsByNameAndJobRecruitment(candidateRequest.getFirstName() + " " +
                        candidateRequest.getLastName(), job_recruitment)) {
            candidateRepository.save(
                    new Candidate(candidateRequest.getFirstName() + " " + candidateRequest.getLastName(),
                            upLoadedFile.getWebContentLink(),
                            LocalDate.now(),
                            candidateRequest.getDateOfBirth(),
                            candidateRequest.getEmail(),
                            candidateRequest.getContact(),
                            job_recruitment
                            )
            );

            long candidateCountForJob = candidateRepository.countByJobRecruitment(job_recruitment);
            if (!job_recruitment.getExpired_date().isBefore(LocalDate.now()) &&
                    candidateCountForJob >= job_recruitment.getQuantity() &&
                    !job_recruitment.getStatus().equals(2)) {
                job_recruitmentRepository.updateJobStatus(job_recruitment.getId(), 2);
            }
            
        }
    }

    @Transactional
    public void promoteCandidate(Long candidateId) {
        if (!candidateRepository.existsById(candidateId)) {
            throw new IllegalStateException("There is no Candidate with that id");
        }
        Candidate candidate = candidateRepository.getById(candidateId);
        employeeService.insertEmployeeById(
                null,
                new EmployeeRequest(
                        new JobDetailInputParams(
                                LocalDate.now(),
                                candidate.getJobRecruitment().getHas().getPosition().getId(),
                                "25%",
                                candidate.getJobRecruitment().getDepartment().getId(),
                                candidate.getJobRecruitment().getHas().getPosition().getSalaryGroup(),
                                candidate.getJobRecruitment().getHas().getPosition().getMin_salary(),
                                1L,
                                null
                        ),
                        new PersonalDetailInputParams(
                                candidate.getName().substring(0, candidate.getName().indexOf(' ') == -1 ? candidate.getName().length() : candidate.getName().indexOf(' ')),
                                candidate.getName().substring(candidate.getName().indexOf(' ') == -1 ? candidate.getName().length() : candidate.getName().indexOf(' ') + 1),
                                candidate.getEmail(),
                                candidate.getContact(),
                                "male",
                                candidate.getDateOfBirth(),
                                null,
                                null
                        ),
                        null,
                        null
                )
        );

        candidateRepository.deleteCandidateById(candidateId);
    }
}
