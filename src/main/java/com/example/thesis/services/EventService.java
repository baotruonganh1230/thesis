package com.example.thesis.services;

import com.example.thesis.entities.*;
import com.example.thesis.repositories.AccountEventRelRepository;
import com.example.thesis.repositories.AccountRepository;
import com.example.thesis.repositories.EventRepository;
import com.example.thesis.requests.SendEventRequest;
import com.example.thesis.requests.UpdateEventRequest;
import com.example.thesis.responses.EventResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final AccountEventRelRepository accountEventRelRepository;
    private final DepartmentService departmentService;
    private final AccountRepository accountRepository;

    public List<EventResponse> getEvents(Long userId, String toDateTimeString, String fromDateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        LocalDateTime fromDateTime = LocalDateTime.parse(fromDateTimeString, formatter);
        LocalDateTime toDateTime = LocalDateTime.parse(toDateTimeString, formatter);

        return eventRepository.getEventsByTimePeriod(fromDateTime, toDateTime)
                .stream()
                .filter(event -> {
                    List<Long> accountIds
                            = accountEventRelRepository.getAccountIdsByEventId(
                                    event.getId());

                    return accountIds.contains(userId);
                })
                .map(event ->
                    new EventResponse(
                            event.getNotes(),
                            event.getTitle(),
                            event.getTime()
                    )
                )
                .collect(Collectors.toList());
    }

    public void updateEvent(UpdateEventRequest updateEventRequest) {
        if (!eventRepository.existsById(updateEventRequest.getId())) {
            throw new IllegalStateException("There is no event with that id");
        }

        eventRepository.updateEventById(
                updateEventRequest.getId(),
                updateEventRequest.getNotes(),
                updateEventRequest.getTitle(),
                updateEventRequest.getTime()
        );
    }

    @Transactional
    public void insertEvent(SendEventRequest sendEventRequest) {
        if (!accountRepository.existsById(sendEventRequest.getUserId())) {
            throw new IllegalStateException("There is no account with that id");
        }

        Account account = accountRepository.getByid(sendEventRequest.getUserId());

        if (account.getEmployee() == null) return;

        Department department = account.getEmployee().getWorksIn().getDepartment();

        List<Department> allSubDepartmentsIncludeThis =
                departmentService.getAllSubDepartmentsIncludeThis(department);

        Event savedEvent = eventRepository.save(
                new Event(
                        sendEventRequest.getNotes(),
                        sendEventRequest.getTitle(),
                        sendEventRequest.getTime()
                )
        );

        for (Department subDepartment : allSubDepartmentsIncludeThis) {
            for (Works_In works_in : subDepartment.getWorks_ins()) {
                Employee employe = works_in.getEmployee();
                Account account1 = accountRepository.getAccountByEid(employe.getId());

                if (account1 == null) continue;

                if (!accountEventRelRepository.existsByEventAndAccount(savedEvent, account1)) {
                    accountEventRelRepository.insertAccountEventRel(
                            account1.getId(),
                            savedEvent.getId()
                    );
                }
            }
        }
    }
}
