package com.example.thesis.services;

import com.example.thesis.entities.Attendance;
import com.example.thesis.entities.Employee;
import com.example.thesis.entities.Leaves;
import com.example.thesis.repositories.AttendanceRepository;
import com.example.thesis.repositories.LeavesRepository;
import com.example.thesis.responses.AttendanceList;
import com.example.thesis.responses.AttendanceResponse;
import com.example.thesis.responses.CheckinResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final LeavesRepository leavesRepository;

    @Transactional
    public AttendanceList getAllAttendances(String week, Long departmentId, Optional<Integer> page) {
        List<Attendance> attendanceList;
        List<String> workingDays = null;

        if (week != null) {
            workingDays = new ArrayList<>();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

            //convert String to LocalDate
            LocalDate dayPassed = LocalDate.parse(week, formatter);
            LocalDate fromDate = dayPassed.with(DayOfWeek.MONDAY);
            LocalDate toDate = fromDate.plusDays(5);
            for (int i = 0; i < 6; i++) {
                workingDays.add(fromDate.plusDays(i) + "T00:00:00.000Z");
            }

            attendanceList = attendanceRepository.findAllAttendancesFromdateTodate(fromDate.toString(), toDate.toString());
        } else {
            attendanceList = attendanceRepository.findAll();
        }

        if (departmentId != null) {
            List<Long> subDepartmentIds = departmentService.getAllSubDepartmentIdsIncludeThis(departmentId);

            List<Attendance> attendances = attendanceList.stream()
                    .filter(attendance -> (subDepartmentIds.contains(attendance
                            .getEmployee()
                            .getWorksIn()
                            .getDepartment()
                            .getId())))
                    .collect(Collectors.toList());
            List<Employee> employeeList = employeeService.findAllEmployeeByDepartmentIncludeSub(departmentId);
            return getAttendanceList(page, attendances, workingDays, employeeList);
        } else {
            List<Employee> employeeList = employeeService.findAll();
            return getAttendanceList(page, attendanceList, workingDays, employeeList);
        }
    }

    private AttendanceList getAttendanceList(Optional<Integer> page, List<Attendance> attendanceList, List<String> workingDays, List<Employee> employeeList) {
        List<AttendanceResponse> attendanceResponses = getAttendanceResponses(attendanceList, workingDays, employeeList);
        PagedListHolder<AttendanceResponse> pages = new PagedListHolder<>(
                attendanceResponses,
                new MutableSortDefinition(
                        "name",
                        true,
                        true
                ));
        pages.resort();
        pages.setPage(page.orElse(0)); //set current page number
        pages.setPageSize(10); // set the size of page

        List<AttendanceResponse> pageList = pages.getPageList();

        return new AttendanceList(
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

    private List<AttendanceResponse> getAttendanceResponses(List<Attendance> attendanceList, List<String> workingDays, List<Employee> employeeList) {
        List<Employee> employeesHaveAttendance = attendanceList.stream()
                .map(Attendance::getEmployee)
                .collect(Collectors.toList());

        for (Employee employee : employeeList) {
            if (!employeesHaveAttendance.contains(employee)) {
                attendanceList.add(new Attendance(
                        null,
                        employee,
                        LocalDate.parse(workingDays.get(0).substring(0,10)),
                        null
                ));
            }

        }
        List<AttendanceResponse> attendanceResponses = convertListAttendanceToAttendanceResponse(attendanceList);
        return addNullAndGroupAttendanceInWeek(attendanceResponses, workingDays);
    }

    private List<AttendanceResponse> convertListAttendanceToAttendanceResponse(List<Attendance> attendanceList) {
        return attendanceList.stream().map(attendance ->
                new AttendanceResponse(
                        attendance.getEmployee().getId(),
                        attendance.getEmployee().getFirstName() + " " +
                        attendance.getEmployee().getLastName(),
                        attendance.getEmployee().getWorksIn() == null ? null : attendance.getEmployee().getWorksIn().getDepartment().getName(),
                        attendance.getEmployee().getPosition() == null ? null : attendance.getEmployee().getPosition().getName(),
                        attendance.getCheckins() != null ? attendance.getCheckins().stream().map(checkin ->
                                new CheckinResponse(
                                        checkin.getStatus(),
                                        checkin.getDate(),
                                        checkin.getTime_in(),
                                        checkin.getTime_out()
                                )
                        ).collect(Collectors.toList()) : new ArrayList<>(List.of(new CheckinResponse(
                                2,
                                attendance.getDate(),
                                null,
                                null
                        ))))).collect(Collectors.toList());
    }

    private List<AttendanceResponse> addNullAndGroupAttendanceInWeek(
            List<AttendanceResponse> attendanceResponses,
            List<String> workingDays
            ) {

        Map<String, List<AttendanceResponse>> groupByEmployeeName =
                attendanceResponses
                        .stream()
                        .collect(Collectors.groupingBy(AttendanceResponse::getName));
        List<AttendanceResponse> attendanceResponsesReturned = new ArrayList<>();

        for (Map.Entry<String, List<AttendanceResponse>> entry : groupByEmployeeName.entrySet()) {
            List<AttendanceResponse> empAttendance = entry.getValue();
            AttendanceResponse prototype = empAttendance.get(0);
            List<String> avaiableDays = empAttendance
                    .stream()
                    .map(attendanceResponse -> attendanceResponse.getCheckins().get(0).getDate())
                    .collect(Collectors.toList());
            for (int i = 1; i < empAttendance.size(); i++) {
                prototype.getCheckins().add(empAttendance.get(i).getCheckins().get(0));
            }
            if (avaiableDays.size() < 6 && workingDays != null) {
                for (String day : workingDays) {
                    if (!avaiableDays.contains(day)){
                        int status = 2;
                        LocalDate missingDay = LocalDate.parse(day.substring(0,10));
                        Leaves leaves = leavesRepository.findByEmployeeAndDate(prototype.getEmployeeId(), missingDay);
                        if (leaves != null) {
                            if (leaves.getType().getIs_paid()) {
                                status = 3;
                            } else {
                                status = 4;
                            }
                        }
                        prototype.getCheckins().add(new CheckinResponse(
                                status,
                                day,
                                null,
                                null
                        ));

                    }
                }
            }
            prototype.getCheckins().sort(Comparator.comparing(CheckinResponse::getDate));
            attendanceResponsesReturned.add(prototype);
        }

        return attendanceResponsesReturned;
    }
}
