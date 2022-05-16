package com.example.thesis.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UnauthorizedJob_RecruitmentResponse {
    private Long id;
    private String positionTitle;
    private String departmentName;
    private String postContent;
}
