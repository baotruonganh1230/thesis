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
    private String appliedDate;
    private String email;
    private String contact;

    public CandidateResponse(Long id, Long jobRecruitmentId, String name, String jobTitle, String departmentName, LocalDate appliedDate, String email, String contact) {
        this.id = id;
        this.jobRecruitmentId = jobRecruitmentId;
        this.name = name;
        this.jobTitle = jobTitle;
        this.departmentName = departmentName;
        this.appliedDate = appliedDate.toString() + "T00:00:00.000Z";
        this.email = email;
        this.contact = contact;
    }
}
