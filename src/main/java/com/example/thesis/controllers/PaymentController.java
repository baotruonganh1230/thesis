package com.example.thesis.controllers;

import com.example.thesis.services.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/")
@AllArgsConstructor
public class PaymentController {
    private PaymentService paymentService;

    @GetMapping("payment")
    public ResponseEntity<?> getPayment(@RequestParam(required=false) String month, @RequestParam(required=false) Long employeeId) {
        return new ResponseEntity<>(paymentService.getPayment(month, employeeId),
                HttpStatus.OK);
    }
}
