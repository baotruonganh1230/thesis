package com.example.thesis.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @Fetch(FetchMode.JOIN)
    @OneToMany(
            mappedBy = "event",
            cascade = CascadeType.ALL
    )
    private Set<AccountEventRel> accountEventRels;

    @Column(columnDefinition = "TEXT")
    private String notes;

    private String title;

    private LocalDateTime time;

    public Event(String notes, String title, LocalDateTime time) {
        this.notes = notes;
        this.title = title;
        this.time = time;
    }
}
