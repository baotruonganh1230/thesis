package com.example.thesis.registration.token;

import com.example.thesis.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumns ({
            @JoinColumn(name="account_eid", referencedColumnName = "eid"),
            @JoinColumn(name="account_roleid", referencedColumnName = "roleid"),
    })
    private Account account;

    public ConfirmationToken(String token,
                             LocalDateTime createdAt,
                             LocalDateTime expiresAt,
                             Account account) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.account = account;
    }
}
