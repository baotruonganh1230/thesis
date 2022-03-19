package com.example.thesis.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CandidateResponse {
    private Long id;
    private Long jobRecruitmentId;
    private String name;
    private String jobTitle;
    private String departmentName;
    private LocalDate appliedDate;
    private String email;
    private String contact;
}
