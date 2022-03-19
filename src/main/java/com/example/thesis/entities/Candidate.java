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

    @OneToOne(cascade = CascadeType.REMOVE, targetEntity = Candidate_CV.class)
    @JoinColumn(name = "cv_file_id", referencedColumnName="id")
    private Candidate_CV candidate_cv;

    private LocalDate appliedDate;

    private String email;

    private String contact;

    @Fetch(FetchMode.JOIN)
    @ManyToOne(cascade=CascadeType.ALL, targetEntity = Job_Recruitment.class)
    @JoinColumn(name="job_recruitment_id", referencedColumnName="id")
    private Job_Recruitment job_recruitment;

    public Candidate() {
    }

}
