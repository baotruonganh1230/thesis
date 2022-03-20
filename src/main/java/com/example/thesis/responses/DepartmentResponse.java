package com.example.thesis.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class DepartmentResponse {
    private Long id;
    private String type;
    private String name;
    private Integer peopleNumber;
    private String description;
    private Long headOfUnitId;
    private Long mangerOfUnitId;
    private Set<DepartmentResponse> subUnits;
}
