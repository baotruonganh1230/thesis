package com.example.thesis.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LeaveEmployeeList {
    private List<LeaveDetail> leaveEmployeeList;
}