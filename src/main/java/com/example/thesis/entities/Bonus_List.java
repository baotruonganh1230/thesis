package com.example.thesis.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@EqualsAndHashCode(exclude = {"employee"})
@AllArgsConstructor
@NoArgsConstructor
public class Bonus_List {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bonusName;

    @Column(precision = 16, scale = 2)
    private BigDecimal amount;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, targetEntity = Employee.class)
    @JoinColumn(name="eid", referencedColumnName="id")
    private Employee employee;
}
