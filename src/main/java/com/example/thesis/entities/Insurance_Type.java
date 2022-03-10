package com.example.thesis.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
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

    public Insurance_Type(Long id, String name, BigDecimal rate_of_payment) {
        this.id = id;
        this.name = name;
        this.rate_of_payment = rate_of_payment;
    }
}
