package com.example.thesis.services;

import com.example.thesis.entities.Candidate;
import com.example.thesis.repositories.CandidateRepository;
import com.example.thesis.responses.CandidateResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CandidateService {
    private final CandidateRepository candidateRepository;

    public List<CandidateResponse> getCandidatesByJob_RecruitmentId(Long job_recruitmentId) {
        List<Candidate> candidates = candidateRepository.findAllByJob_RecruitmentId(job_recruitmentId);
        return candidates.stream().map(candidate ->
                new CandidateResponse(candidate.getId(),
                        candidate.getJob_recruitment().getId(),
                        candidate.getName(),
                        candidate.getJob_recruitment().getHas().getPosition().getName(),
                        candidate.getJob_recruitment().getDepartment().getName(),
                        candidate.getAppliedDate(),
                        candidate.getEmail(),
                        candidate.getContact()))
                .collect(Collectors.toList());
    }
}
