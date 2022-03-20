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
    private Long cityId;
    private Long districtId;
    private Long wardId;
    private String address;

    @Override
    public String toString() {
        return "{" +
                "\"cityId\":" + cityId.toString() +
                ", \"districtId\":" + districtId.toString() +
                ", \"wardId\":" + wardId.toString() +
                ", \"address\":" + "\"" + address + "\""  +
                '}';
    }
}
