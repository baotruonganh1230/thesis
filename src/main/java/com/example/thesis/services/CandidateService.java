package com.example.thesis.services;

import com.example.thesis.entities.Candidate;
import com.example.thesis.entities.Job_Recruitment;
import com.example.thesis.repositories.CandidateRepository;
import com.example.thesis.repositories.Job_RecruitmentRepository;
import com.example.thesis.requests.CandidateRequest;
import com.example.thesis.responses.CandidateResponse;
import lombok.AllArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CandidateService {
    private final CandidateRepository candidateRepository;
    private final EmployeeService employeeService;
    private final GoogleDriveService googleDriveService;
    private final Job_RecruitmentRepository job_recruitmentRepository;

    public List<CandidateResponse> getCandidatesByJob_RecruitmentId(Long job_recruitmentId) {
        if (job_recruitmentId != null) {
            List<Candidate> candidates = candidateRepository.findAllByJob_RecruitmentId(job_recruitmentId);
            return candidates.stream().map(candidate ->
                            new CandidateResponse(candidate.getId(),
                                    candidate.getJobRecruitment().getId(),
                                    candidate.getName(),
                                    candidate.getJobRecruitment().getHas().getPosition().getName(),
                                    candidate.getJobRecruitment().getDepartment().getName(),
                                    candidate.getAppliedDate(),
                                    candidate.getEmail(),
                                    candidate.getContact()))
                    .collect(Collectors.toList());
        } else {
            return candidateRepository.findAll().stream().map(candidate ->
                            new CandidateResponse(candidate.getId(),
                                    candidate.getJobRecruitment().getId(),
                                    candidate.getName(),
                                    candidate.getJobRecruitment().getHas().getPosition().getName(),
                                    candidate.getJobRecruitment().getDepartment().getName(),
                                    candidate.getAppliedDate(),
                                    candidate.getEmail(),
                                    candidate.getContact()))
                    .collect(Collectors.toList());
        }
    }

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

        if (!candidateRepository.existsByNameAndJobRecruitment(candidateRequest.getLastName() +
                " " +
                candidateRequest.getFirstName(), job_recruitment)) {
            candidateRepository.save(
                    new Candidate(candidateRequest.getLastName() + " " + candidateRequest.getFirstName(),
                            upLoadedFile.getWebContentLink(),
                            LocalDate.now(),
                            candidateRequest.getDateOfBirth(),
                            candidateRequest.getEmail(),
                            candidateRequest.getContact(),
                            job_recruitment
                            )
            );
        }
    }

}
