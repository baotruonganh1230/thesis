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

    @Lob
    private Blob cv;

    private String email;

    @ManyToMany(mappedBy = "candidates")
    private Set<Job_Recruitment> job_recruitments;

    public Candidate() {
    }

    public Candidate(String name, Blob cv, String email) {
        this.name = name;
        this.cv = cv;
        this.email = email;
    }
}
