package com.example.thesis.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Insurance_Type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(precision = 16, scale = 2)
    private BigDecimal rate_of_payment;

    @OneToMany(
            mappedBy = "type",
            cascade = CascadeType.ALL
    )
    private List<Insurance> insurances = new ArrayList<>();
}
