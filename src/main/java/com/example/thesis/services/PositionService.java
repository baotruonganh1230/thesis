package com.example.thesis.services;

import com.example.thesis.entities.Position;
import com.example.thesis.repositories.PositionRepository;
import com.example.thesis.responses.PositionResponse;
import com.google.common.collect.Lists;
import com.mysql.cj.exceptions.DataReadException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PositionService {
    private final PositionRepository repository;

    public List<PositionResponse> getPostions() {
        List<PositionResponse> positionResponses = new ArrayList<>();

        Lists.newArrayList(repository.findAll()).forEach((Position position) -> {
            PositionResponse positionResponse = new PositionResponse(
                    position.getId(),
                    position.getName(),
                    position.getDescription(),
                    position.getNote());
            positionResponses.add(positionResponse);
        });

        return positionResponses;
    }

    public PositionResponse getPostionById(Long id) {
        Position position = repository.getById(id);
        return new PositionResponse(
                position.getId(),
                position.getName(),
                position.getDescription(),
                position.getNote());
    }

    public void updatePositionById(Long id, Position position) {
        if (!repository.existsById(id)) {
            throw new DataReadException("There is no position with that id");
        }
        repository.setPositionById(
                id,
                position.getName(),
                position.getDescription(),
                position.getNote());
    }

    public void insertPositionById(Position position) {
        repository.save(position);
    }
}
