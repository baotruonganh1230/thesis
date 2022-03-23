package com.example.thesis.services;

import com.example.thesis.entities.Account;
import com.example.thesis.entities.Employee;
import com.example.thesis.entities.Leaves;
import com.example.thesis.repositories.AccountRepository;
import com.example.thesis.repositories.Leave_TypeRepository;
import com.example.thesis.repositories.LeavesRepository;
import com.example.thesis.requests.LeaveRequest;
import com.example.thesis.requests.UpdateLeaveRequest;
import com.example.thesis.responses.LeaveDetail;
import com.example.thesis.responses.LeaveResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LeaveService {
    private final LeavesRepository leavesRepository;
    private final AccountRepository accountRepository;
    private final Leave_TypeRepository leave_typeRepository;

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

    public void insertRequestLeave(LeaveRequest leaveRequest) {
        Employee employee = accountRepository.getById(leaveRequest.getUserId()).getEmployee();
        leavesRepository.save(
                new Leaves(
                        employee,
                        leave_typeRepository.getById(leaveRequest.getLeaveType()),
                        leaveRequest.getFromDate(),
                        leaveRequest.getToDate(),
                        LocalDate.now(),
                        leaveRequest.getAmount(),
                        null,
                        leaveRequest.getReason()
                )
        );
    }

    public List<LeaveResponse> getLeave(Long userId) {
        Account account = accountRepository.getById(userId);
        return leavesRepository.getAllByEmployee(account.getEmployee())
                .stream()
                .map(leaves ->
                        new LeaveResponse(
                                leaves.getType().getId(),
                                leaves.getTotal(),
                                leaves.getFrom_date(),
                                leaves.getTo_date(),
                                userId,
                                leaves.getReason(),
                                leaves.getStatus()
                        )).collect(Collectors.toList());
    }
}
