package com.example.thesis.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String location;

    private String name;

    private Integer peopleNumber;

    private String type;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "headOfUnit")
    private Set<Department> subUnits = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    private Department headOfUnit;

    @JsonIgnore
    @OneToMany(
            mappedBy = "department",
            cascade = CascadeType.ALL
    )
    private List<Job_Recruitment> job_recruitments = new ArrayList<>();

    @JsonIgnore
    @OneToOne(
            mappedBy = "department",
            cascade = CascadeType.ALL
    )
    private Manage manage;

    @JsonIgnore
    @OneToMany(
            mappedBy = "department",
            cascade = CascadeType.ALL
    )
    private List<Works_In> works_ins;

    public Department() {
    }

}
