package com.example.thesis.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Candidate_CV {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cv_file_name;

    private String cv_file_type;

    private Long cv_file_size;

    @Lob
    private Byte[] cv_file_content;

    @OneToOne(
            mappedBy = "candidate_cv",
            cascade = CascadeType.ALL
    )
    private Candidate candidate;
}
