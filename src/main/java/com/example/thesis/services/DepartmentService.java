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

    private DepartmentResponse convertFromDepartmentToDepartmentResponse(Set<Long> idSet, Department department) {
        if (department != null) {
            Set<DepartmentResponse> departmentResponses = new HashSet<>();
            List<Department> subDepartments = repository.findAllByHeadOfUnit(department);
            for (Department subDepartment : subDepartments) {
                if (!idSet.contains(subDepartment.getId())) {
                    departmentResponses.add(convertFromDepartmentToDepartmentResponse(idSet, subDepartment));
                    idSet.add(subDepartment.getId());
                }
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
                for (Department subDepartment : subDepartments) {
                    if (!idExistedInSet(departmentResponses, subDepartment.getId())) {
                        addToDepartmentResponseSetNoNested(
                                departmentResponses, subDepartment);
                    }
                }
            }
        }
    }

    private boolean idExistedInSet(Set<DepartmentResponse> departmentResponses, Long id) {
        for (DepartmentResponse departmentResponse : departmentResponses) {
            if (departmentResponse.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public Set<DepartmentResponse> getDepartments(Boolean nested) {
        Set<DepartmentResponse> departmentResponses = new HashSet<>();

        if (nested) {
            Set<Long> idSet = new HashSet<>();
            Lists.newArrayList(repository.findAll()).forEach((Department department) -> {
                if (!idSet.contains(department.getId())) {
                    idSet.add(department.getId());
                    departmentResponses.add(convertFromDepartmentToDepartmentResponse(idSet, department));
                }

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
        Set<Long> idSet = new HashSet<>();
        idSet.add(department.getId());
        return convertFromDepartmentToDepartmentResponse(idSet, department);
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
            for (DepartmentRequest subDepartment : departmentRequest.getSubUnits()) {
                updateDepartmentById(subDepartment.getId(), subDepartment);
                repository.setHeadOfUnit(subDepartment.getId(), departmentRequest.getId());
            }
        }

    }

    public void insertDepartmentById(DepartmentRequest departmentRequest) {

        repository.insertDepartmentById(departmentRequest.getLocation(),
                departmentRequest.getType(),
                departmentRequest.getName(),
                departmentRequest.getPeopleNumber(),
                departmentRequest.getDescription(),
                departmentRequest.getHeadOfUnitId());

        if (departmentRequest.getSubUnits() != null) {
            for (DepartmentRequest subDepartment : departmentRequest.getSubUnits()) {
                insertDepartmentById(subDepartment);
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
