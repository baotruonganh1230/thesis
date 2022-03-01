package com.example.thesis.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class DepartmentRequest {
    private Long id;

    private String location;

    private String name;

    private Integer peopleNumber;

    private String type;

    private String description;

    private Long headOfUnitId;

    private Set<DepartmentRequest> subUnits = new HashSet<>();
}
