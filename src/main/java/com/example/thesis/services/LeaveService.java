package com.example.thesis.services;

import com.example.thesis.entities.*;
import com.example.thesis.repositories.*;
import com.example.thesis.requests.LeaveRequest;
import com.example.thesis.requests.UpdateLeaveRequest;
import com.example.thesis.responses.LeaveDetail;
import com.example.thesis.responses.LeaveEmployeeList;
import com.example.thesis.responses.LeaveResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    private final CheckinRepository checkinRepository;
    private final AttendanceRepository attendanceRepository;

    public LeaveEmployeeList getAllLeaves(Long departmentId, String date, Optional<Integer> page, Optional<String> sortBy, Optional<String> sortOrder) {
        List<Leaves> leavesList;

        if (date != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

            //convert String to LocalDate
            LocalDate applicationDate = LocalDate.parse(date, formatter);
            leavesList = leavesRepository.findAllByApplicationDate(applicationDate);
        } else {
            leavesList = leavesRepository.findAll();
        }

        if (departmentId != null) {
            List<Long> subDepartmentIds = departmentService.getAllSubDepartmentIdsIncludeThis(departmentId);

            List<Leaves> filteredLeavesList = leavesList.stream()
                    .filter(leave -> (subDepartmentIds.contains(leave
                            .getEmployee()
                            .getWorksIn()
                            .getDepartment()
                            .getId())))
                    .collect(Collectors.toList());

            return getLeaveEmployeeList(page, sortBy, sortOrder, filteredLeavesList);
        } else {
            return getLeaveEmployeeList(page, sortBy, sortOrder, leavesList);
        }
    }

    private LeaveEmployeeList getLeaveEmployeeList(Optional<Integer> page, Optional<String> sortBy, Optional<String> sortOrder, List<Leaves> leavesList) {
        List<LeaveDetail> leaveDetails = convertListLeaveToLeaveDetail(leavesList);
        PagedListHolder<LeaveDetail> pages = new PagedListHolder<>(
                leaveDetails,
                new MutableSortDefinition(
                        sortBy.orElse("id"),
                        true,
                        sortOrder.orElse("asc").equalsIgnoreCase("asc")
                ));
        pages.resort();
        pages.setPage(page.orElse(0)); //set current page number
        pages.setPageSize(10); // set the size of page

        List<LeaveDetail> pageList = pages.getPageList();

        return new LeaveEmployeeList(
                page.orElse(0) >= pages.getPageCount() ? new ArrayList<>() : pageList,
                page.orElse(0).equals(pages.getPageCount() - 1),
                pages.getPageCount(),
                pages.getNrOfElements(),
                pages.getPageSize(),
                page.orElse(0),
                page.orElse(0).equals(0),
                page.orElse(0) >= pages.getPageCount() ? 0 : pages.getPageList().size(),
                pages.getPageList().size() == 0
        );
    }

    private List<LeaveDetail> convertListLeaveToLeaveDetail(List<Leaves> leavesList) {
        return leavesList.stream().map(leave ->
                new LeaveDetail(leave.getId(),
                        leave.getEmployee().getFirstName() + " " + leave.getEmployee().getLastName(),
                        leave.getEmployee().getWorksIn().getDepartment().getName(),
                        leave.getApplicationDate(),
                        leave.getFromDate(),
                        leave.getToDate(),
                        leave.getTotal(),
                        leave.getStatus(),
                        leave.getType().getName()))
                .collect(Collectors.toList());
    }

    public void updateLeavesStatusByIds(UpdateLeaveRequest updateLeaveRequest) {
        for (Long id : updateLeaveRequest.getListIds()) {
            Leaves leave = leavesRepository.getLeaveById(id);
            if (leave != null) {
                leave.setStatus(updateLeaveRequest.getStatus());
                leavesRepository.save(leave);
            }
            int i = 0;
            while (!leave.getFromDate().plusDays(i).isAfter(leave.getToDate())) {
                Attendance attendance = attendanceRepository.findAttendanceOfEmployeeByDate(leave.getEmployee().getId(), leave.getFromDate().plusDays(i));
                if (attendance != null) {
                    Checkin checkin = checkinRepository.findByAttendanceId(attendance.getId());
                    if (checkin != null) {
                        int newStatus = leave.getType().getId() == 1 ? 3 : 4;
                        if (checkin.getStatus() != newStatus) {
                            checkinRepository.setStatus(checkin.getAttendanceId(), newStatus);
                        }
                    }
                }
                i++;
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
                                leaves.getStatus(),
                                leaves.getType().getName()
                        )).collect(Collectors.toList());
    }
}
