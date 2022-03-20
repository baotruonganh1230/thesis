package com.example.thesis.responses;

import com.example.thesis.requests.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


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
    private LocalDate dateOfBirth;
    private Address permanentAddress;
    private Address temporaryAddress;

    public void setDateOfBirth(String dateOfBirthString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        //convert String to LocalDate
        this.dateOfBirth = LocalDate.parse(dateOfBirthString, formatter);
    }
}
