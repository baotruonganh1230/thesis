package com.example.thesis.entities;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@EqualsAndHashCode(exclude = {"employee"})
@AllArgsConstructor
@NoArgsConstructor
public class Works_In implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eid;

    @MapsId
    @OneToOne(cascade = CascadeType.REMOVE, targetEntity = Employee.class)
    @JoinColumn(name="eid", referencedColumnName="id")
    private Employee employee;

    @Fetch(FetchMode.JOIN)
    @ManyToOne(cascade = CascadeType.REMOVE, targetEntity = Department.class)
    @JoinColumn(name="did", referencedColumnName="id")
    private Department department;
}
