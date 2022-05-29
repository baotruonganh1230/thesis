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
public class PaymentBonusRel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Fetch(FetchMode.JOIN)
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, targetEntity = Payment.class)
    @JoinColumn(name="payment_id", referencedColumnName="id")
    private Payment payment;

    @Fetch(FetchMode.JOIN)
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, targetEntity = Bonus_List.class)
    @JoinColumn(name="bonus_id", referencedColumnName="id")
    private Bonus_List bonusList;
}
