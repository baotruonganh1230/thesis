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
public class AccountEventRel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Fetch(FetchMode.JOIN)
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, targetEntity = Event.class)
    @JoinColumn(name="eventid", referencedColumnName="id")
    private Event event;

    @Fetch(FetchMode.JOIN)
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, targetEntity = Account.class)
    @JoinColumn(name="accountid", referencedColumnName="id")
    private Account account;
}
