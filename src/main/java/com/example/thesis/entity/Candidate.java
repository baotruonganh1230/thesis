package com.example.thesis.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Blob;
import java.util.Set;

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

    private String email;

    @ManyToMany(mappedBy = "candidates")
    private Set<Job_Recruitment> job_recruitments;

    public Candidate() {
    }

}
