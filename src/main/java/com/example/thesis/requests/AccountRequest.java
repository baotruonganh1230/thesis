package com.example.thesis.requests;

import com.example.thesis.entities.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequest {
    private Long eid;
    private Long roleid;
    private String username;
    private String password;
    private AccountStatus status;
}
