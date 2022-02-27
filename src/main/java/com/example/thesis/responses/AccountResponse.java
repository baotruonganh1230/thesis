package com.example.thesis.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AccountResponse {
    private Long eid;
    private String username;
    private Long roleid;
    private String password;
    private String accountStatus;
}
