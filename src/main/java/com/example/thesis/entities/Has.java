package com.example.thesis.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Has {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Fetch(FetchMode.JOIN)
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, targetEntity = Position.class)
    @JoinColumn(name="posid", referencedColumnName="id")
    private Position position;

    @Fetch(FetchMode.JOIN)
    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, targetEntity = Job_Recruitment.class)
    @JoinColumn(name="jrecruitid", referencedColumnName="id")
    private Job_Recruitment job_recruitment;
}
