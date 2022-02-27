package com.example.thesis.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@EqualsAndHashCode
public class Has implements Serializable {
    @Id
    @OneToOne(cascade = CascadeType.REMOVE, targetEntity = Position.class)
    @JoinColumn(name="posid", referencedColumnName="id")
    private Position position;

    @OneToOne(cascade = CascadeType.REMOVE, targetEntity = Job_Recruitment.class)
    @JoinColumn(name="jrecruitid", referencedColumnName="id")
    private Job_Recruitment job_recruitment;
}
