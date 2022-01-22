package com.example.thesis.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Job_Recruitment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String job_description;

    private LocalDate from_date;

    private LocalDate to_date;

    @OneToOne(
            mappedBy = "job_recruitment",
            cascade = CascadeType.ALL
    )
    private Has has;

    @ManyToMany
    @JoinTable(name = "Apply",
            joinColumns = @JoinColumn(name = "jrecruitid"),
            inverseJoinColumns = @JoinColumn(name = "cid"))
    private Set<Candidate> candidates = new HashSet<>();

    public Job_Recruitment() {
    }

    public Job_Recruitment(String job_description, LocalDate from_date, LocalDate to_date) {
        this.job_description = job_description;
        this.from_date = from_date;
        this.to_date = to_date;
    }
}
