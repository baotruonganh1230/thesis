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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@AllArgsConstructor
public class LeaveService {
    private final LeavesRepository leavesRepository;
    private final AccountRepository accountRepository;
    private final Leave_TypeRepository leave_typeRepository;
    private final DepartmentService departmentService;

    public Page<LeaveDetail> getAllLeaves(Long departmentId, String date, Optional<Integer> page, Optional<String> sortBy, Optional<String> sortOrder) {
        if (sortBy.isPresent() && sortBy.get().equals("employeeName")) {
            sortBy = Optional.of("employee.firstName");
        } else if (sortBy.isPresent() && (sortBy.get().equals("departmentName") || sortBy.get().equals("unitName"))) {
            sortBy = Optional.of("employee.worksIn.department.name");
        }
        Page<Leaves> leavesPage;

        if (date != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

            //convert String to LocalDate
            LocalDate applicationDate = LocalDate.parse(date, formatter);
            leavesPage = leavesRepository.findAllByApplicationDate(applicationDate,
                    PageRequest.of(
                            page.orElse(0),
                            10,
                            Sort.Direction.fromString(sortOrder.orElse("asc")),
                            sortBy.orElse("id")
                    ));
        } else {
            leavesPage = leavesRepository.findAll(PageRequest.of(
                    page.orElse(0),
                    10,
                    Sort.Direction.fromString(sortOrder.orElse("asc")),
                    sortBy.orElse("id")
            ));
        }

        if (departmentId != null) {
            List<Long> subDepartmentIds = departmentService.getAllSubDepartmentIdsIncludeThis(departmentId);

            List<Leaves> filteredLeavesList = leavesPage.stream()
                    .filter(leave -> (subDepartmentIds.contains(leave
                            .getEmployee()
                            .getWorksIn()
                            .getDepartment()
                            .getId())))
                    .collect(Collectors.toList());

            Page<Leaves> filteredLeavesPage = new PageImpl<>(filteredLeavesList);
            return convertListLeaveToLeaveDetail(filteredLeavesPage);
        } else {
            return convertListLeaveToLeaveDetail(leavesPage);
        }
    }

    private Page<LeaveDetail> convertListLeaveToLeaveDetail(Page<Leaves> leavesPage) {
        return leavesPage.map(leave ->
                new LeaveDetail(leave.getId(),
                        leave.getEmployee().getFirstName() + " " + leave.getEmployee().getLastName(),
                        leave.getEmployee().getWorksIn().getDepartment().getName(),
                        leave.getApplicationDate(),
                        leave.getFromDate(),
                        leave.getToDate(),
                        leave.getTotal(),
                        leave.getStatus()));
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
        List<Leaves> listLeavesOverlap =
                leavesRepository.findAllByEmployeeAndPeriod(
                        employee.getId(),
                        leaveRequest.getFromDate(),
                        leaveRequest.getToDate());

        if (listLeavesOverlap.size() > 0) {
            throw new IllegalStateException("Leave period overlap for this employee!!");
        }

        leavesRepository.save(
                new Leaves(
                        employee,
                        leave_typeRepository.getById(leaveRequest.getLeaveType()),
                        leaveRequest.getFromDate(),
                        leaveRequest.getToDate(),
                        LocalDate.now(),
                        leaveRequest.getAmount() != null ? leaveRequest.getAmount() :
                                Math.toIntExact(DAYS.between(leaveRequest.getFromDate(), leaveRequest.getToDate()) + 1),
                        2,
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
                                leaves.getApplicationDate(),
                                leaves.getFromDate(),
                                leaves.getToDate(),
                                userId,
                                leaves.getReason(),
                                leaves.getStatus()
                        )).collect(Collectors.toList());
    }
}
