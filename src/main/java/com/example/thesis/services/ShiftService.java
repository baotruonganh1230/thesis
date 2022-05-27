package com.example.thesis.services;

import com.example.thesis.entities.Shift;
import com.example.thesis.repositories.ShiftRepository;
import com.example.thesis.requests.ShiftRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ShiftService {
    private final ShiftRepository shiftRepository;

    public List<Shift> getShifts() {
        return shiftRepository.findAll();
    }

    public Shift getShiftById(Long id) {
        Optional<Shift> shiftOptional = shiftRepository.findById(id);
        return shiftOptional.orElse(null);
    }

    public void updateShiftById(Long id, ShiftRequest shiftRequest) {
        if (!shiftRepository.existsById(id)) {
            throw new IllegalStateException("There is no Shift with that id");
        }

        shiftRepository.setShiftById(id,
                shiftRequest.getName(),
                shiftRequest.getFrom(),
                shiftRequest.getTo());
    }

    public void insertShift(ShiftRequest shiftRequest) {
        shiftRepository.save(
                new Shift(
                        null,
                        shiftRequest.getName(),
                        shiftRequest.getFrom(),
                        shiftRequest.getTo()
                )
        );
    }
}
