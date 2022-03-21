package com.example.thesis.responses;

import com.example.thesis.requests.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonalDetailOutputParams {
    private String avatar;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String sex;
    private String dateOfBirth;
    private Address permanentAddress;
    private Address temporaryAddress;

    public PersonalDetailOutputParams(String avatar, String firstName, String lastName, String email, String phone, String sex, LocalDate dateOfBirth, Address permanentAddress, Address temporaryAddress) {
        this.avatar = avatar;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.sex = sex;
        this.dateOfBirth = dateOfBirth.toString() + "'T'00:00:00.000Z";
        this.permanentAddress = permanentAddress;
        this.temporaryAddress = temporaryAddress;
    }
}
