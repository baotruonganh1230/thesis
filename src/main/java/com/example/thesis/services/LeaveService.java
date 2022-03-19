package com.example.thesis.services;

import com.example.thesis.entities.Leaves;
import com.example.thesis.repositories.LeavesRepository;
import com.example.thesis.requests.UpdateLeaveRequest;
import com.example.thesis.responses.LeaveDetail;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LeaveService {
    private final LeavesRepository leavesRepository;

    public List<LeaveDetail> getAllLeaves(Long departmentId) {
        List<Leaves> leavesList = leavesRepository.findAll();

        if (departmentId != null) {
            List<Leaves> filteredLeavesList = leavesList.stream()
                    .filter(leave -> (leave
                            .getEmployee()
                            .getWorks_in()
                            .getDepartment()
                            .getId() == departmentId))
                    .collect(Collectors.toList());
            return convertListLeaveToLeaveDetail(filteredLeavesList);
        } else {
            return convertListLeaveToLeaveDetail(leavesList);
        }
    }

    private List<LeaveDetail> convertListLeaveToLeaveDetail(List<Leaves> leavesList) {
        return leavesList.stream().map(leave ->
                new LeaveDetail(leave.getId(),
                        leave.getEmployee().getFirst_name() + " " + leave.getEmployee().getLast_name(),
                        leave.getEmployee().getWorks_in().getDepartment().getName(),
                        leave.getApplication_date(),
                        leave.getFrom_date(),
                        leave.getTo_date(),
                        leave.getTotal(),
                        leave.getStatus())).collect(Collectors.toList());
    }

    public void updateLeavesStatusByIds(UpdateLeaveRequest updateLeaveRequest) {
        for (Long id : updateLeaveRequest.getListIds()) {
            Leaves leave = leavesRepository.getById(id);
            if (leave != null) {
                leave.setStatus(updateLeaveRequest.getStatus());
                leavesRepository.save(leave);
            }
        }
    }
}
