package com.example.thesis.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceResponse {
    private Long employeeId;
    private String name;
    private String departmentName;
    private String jobTitle;
    private List<CheckinResponse> checkins;
}
