package com.example.thesis.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private String city;
    private String district;
    private String ward;
    private String address;

    @Override
    public String toString() {
        return address + ", " + city + ", " + ward + ", " + district;
    }
}
