package com.example.thesis.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
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
    @Fetch(FetchMode.JOIN)
    private Set<Department> subUnits = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    private Department headOfUnit;

    @JsonIgnore
    @Fetch(FetchMode.JOIN)
    @OneToMany(
            mappedBy = "department",
            cascade = CascadeType.ALL
    )
    private Set<Job_Recruitment> job_recruitments = new HashSet<>();

    @JsonIgnore
    @Fetch(FetchMode.JOIN)
    @OneToOne(
            mappedBy = "department",
            cascade = CascadeType.ALL
    )
    private Manage manage;

    @JsonIgnore
    @Fetch(FetchMode.JOIN)
    @OneToMany(
            mappedBy = "department",
            cascade = CascadeType.ALL
    )
    private List<Works_In> works_ins;

    public Department() {
    }

}
