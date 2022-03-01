package com.example.thesis.services;

import com.example.thesis.entities.Department;
import com.example.thesis.repositories.DepartmentRepository;
import com.example.thesis.requests.DepartmentRequest;
import com.example.thesis.responses.DepartmentResponse;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class DepartmentService {
    private final DepartmentRepository repository;

    private DepartmentResponse convertFromDepartmentToDepartmentResponse(Department department) {
        if (department != null) {
            Set<DepartmentResponse> departmentResponses = new HashSet<>();
            List<Department> subDepartments = repository.findAllByHeadOfUnit(department);
            for (var subDepartment : subDepartments) {
                departmentResponses.add(convertFromDepartmentToDepartmentResponse(subDepartment));
            }
            return new DepartmentResponse(
                    department.getId(),
                    department.getType(),
                    department.getName(),
                    department.getPeopleNumber(),
                    department.getDescription(),
                    (department.getHeadOfUnit() == null) ? null : department.getHeadOfUnit().getId(),
                    departmentResponses);
        }
        return null;
    }

    private void addToDepartmentResponseSetNoNested(
            Set<DepartmentResponse> departmentResponses, Department department) {
        if (department != null) {
            if (!idExistedInSet(departmentResponses, department.getId())) {
                departmentResponses.add(new DepartmentResponse(
                        department.getId(),
                        department.getType(),
                        department.getName(),
                        department.getPeopleNumber(),
                        department.getDescription(),
                        (department.getHeadOfUnit() == null) ? null : department.getHeadOfUnit().getId(),
                        null));
            }

            List<Department> subDepartments = repository.findAllByHeadOfUnit(department);

            if (subDepartments != null) {
                for (var subDepartment : subDepartments) {
                    if (!idExistedInSet(departmentResponses, subDepartment.getId())) {
                        addToDepartmentResponseSetNoNested(
                                departmentResponses, subDepartment);
                    }
                }
            }
        }
    }

    private boolean idExistedInSet(Set<DepartmentResponse> departmentResponses, Long id) {
        for (var departmentResponse : departmentResponses) {
            if (departmentResponse.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public Set<DepartmentResponse> getDepartments(Boolean nested) {
        Set<DepartmentResponse> departmentResponses = new HashSet<>();

        if (nested) {
            Lists.newArrayList(repository.findAll()).forEach((Department department) -> {
                departmentResponses.add(convertFromDepartmentToDepartmentResponse(department));
            });
        } else {
            Lists.newArrayList(repository.findAll()).forEach((Department department) -> {
                addToDepartmentResponseSetNoNested(departmentResponses, department);
            });
        }

        return departmentResponses;
    }

    public DepartmentResponse getDepartment(Long id) {
        Department department = repository.getById(id);
        return convertFromDepartmentToDepartmentResponse(department);
    }

    public void updateDepartmentById(Long id, DepartmentRequest departmentRequest) {
        if (!repository.existsById(id)) {
            throw new IllegalStateException("There is no department with that id");
        }

        repository.setDepartmentById(id,
                departmentRequest.getLocation(),
                departmentRequest.getType(),
                departmentRequest.getName(),
                departmentRequest.getPeopleNumber(),
                departmentRequest.getDescription(),
                departmentRequest.getHeadOfUnitId());

        if (departmentRequest.getSubUnits() != null) {
            for (var subDepartment : departmentRequest.getSubUnits()) {
                updateDepartmentById(subDepartment.getId(), subDepartment);
                repository.setHeadOfUnit(subDepartment.getId(), departmentRequest.getId());
            }
        }

    }

    public void insertDepartmentById(Long id, DepartmentRequest departmentRequest) {
        if (repository.existsById(id)) {
            throw new IllegalStateException("Department already exists " + id);
        }

        repository.insertDepartmentById(id,
                departmentRequest.getLocation(),
                departmentRequest.getType(),
                departmentRequest.getName(),
                departmentRequest.getPeopleNumber(),
                departmentRequest.getDescription(),
                departmentRequest.getHeadOfUnitId());

        if (departmentRequest.getSubUnits() != null) {
            for (var subDepartment : departmentRequest.getSubUnits()) {
                insertDepartmentById(subDepartment.getId(), subDepartment);
                repository.setHeadOfUnit(subDepartment.getId(), departmentRequest.getId());
            }
        }

    }

    public Department save(Department department) {
        return repository.save(department);
    }

    public Long count() {
        return repository.count();
    }
}
