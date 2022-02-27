package com.example.thesis.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Job_RecruitmentResponse {
    private Long id;
    private String title;
    private String description;
    private String note;
}
