package com.example.thesis.services;

import com.example.thesis.entities.Role;
import com.example.thesis.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class RoleService {
    private final RoleRepository repository;

    @Transactional
    public Role save(Role role) {
        return repository.save(role);
    }

    public Long count() {
        return repository.count();
    }
}
