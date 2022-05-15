package com.example.thesis.controllers;

import com.example.thesis.requests.SendEventRequest;
import com.example.thesis.requests.UpdateEventRequest;
import com.example.thesis.services.EventService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/")
@AllArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping("getEvents")
    public ResponseEntity<?> getEvents(@RequestParam Long userId,
                                       @RequestParam String toDateTime,
                                       @RequestParam String fromDateTime) {
        return new ResponseEntity<>(eventService.getEvents(userId, toDateTime, fromDateTime), HttpStatus.OK);
    }

    @PutMapping("updateEvent")
    public ResponseEntity<?> updateEvent(@RequestBody UpdateEventRequest updateEventRequest) {
        eventService.updateEvent(updateEventRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("sendEvent")
    public ResponseEntity<?> insertEvent(@RequestBody SendEventRequest sendEventRequest) {
        eventService.insertEvent(sendEventRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
