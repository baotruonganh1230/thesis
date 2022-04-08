package com.example.thesis.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String cv_file;

    private LocalDate appliedDate;

    private LocalDate dateOfBirth;

    private String email;

    private String contact;

    @Fetch(FetchMode.JOIN)
    @ManyToOne(cascade=CascadeType.ALL, targetEntity = Job_Recruitment.class)
    @JoinColumn(name="job_recruitment_id", referencedColumnName="id")
    private Job_Recruitment jobRecruitment;

    public Candidate() {
    }

    public Candidate(String name, String cv_file, LocalDate appliedDate, LocalDate dateOfBirth, String email, String contact, Job_Recruitment jobRecruitment) {
        this.name = name;
        this.cv_file = cv_file;
        this.appliedDate = appliedDate;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.contact = contact;
        this.jobRecruitment = jobRecruitment;
    }
}
