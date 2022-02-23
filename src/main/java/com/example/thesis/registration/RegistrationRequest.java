package com.example.thesis.registration;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RegistrationRequest {
    private String first_name;

    private String last_name;

    private String email;

    private String phone;

    private String sex;

    private String date_of_birth;

    private String username;

    private String password;

    private String permanent_address;

    private String temporary_address;
}
