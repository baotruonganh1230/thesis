package com.example.thesis.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@EqualsAndHashCode(exclude = {"employee"})
public class Attendance implements Serializable {
    @Id
    private Long eid;

    @JsonIgnore
    @Fetch(FetchMode.JOIN)
    @MapsId
    @ManyToOne(cascade = CascadeType.REMOVE, targetEntity = Employee.class)
    @JoinColumn(name="eid", referencedColumnName="id")
    private Employee employee;

    @Fetch(FetchMode.JOIN)
    @OneToMany(
            mappedBy = "attendance",
            cascade = CascadeType.ALL
    )
    private List<Checkin> checkins =  new ArrayList<>();
}
