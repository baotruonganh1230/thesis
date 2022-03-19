package com.example.thesis.responses;

import com.example.thesis.entities.Checkin;
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
    private String name;
    private String departmentName;
    private String jobTitle;
    private List<Checkin> checkins;
}
