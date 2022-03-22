package com.example.thesis.services;

import com.example.thesis.entities.Department;
import com.example.thesis.entities.Manage;
import com.example.thesis.entities.Works_In;
import com.example.thesis.repositories.DepartmentRepository;
import com.example.thesis.repositories.EmployeeRepository;
import com.example.thesis.repositories.ManageRepository;
import com.example.thesis.repositories.Works_InRepository;
import com.example.thesis.requests.DepartmentRequest;
import com.example.thesis.responses.DepartmentResponse;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final ManageRepository manageRepository;
    private final EmployeeRepository employeeRepository;
    private final Works_InRepository works_inRepository;

    private DepartmentResponse convertFromDepartmentToDepartmentResponse(Set<Long> idSet, Department department) {
        if (department != null) {
            Set<DepartmentResponse> departmentResponses = new HashSet<>();
            List<Department> subDepartments = departmentRepository.findAllByHeadOfUnit(department);
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
                    (department.getManage() == null) ? null : department.getManage().getDepartment().getId(),
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
                        (department.getManage() == null) ? null : department.getManage().getDepartment().getId(),
                        null));
            }

            List<Department> subDepartments = departmentRepository.findAllByHeadOfUnit(department);

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

    @Transactional
    public Set<DepartmentResponse> getDepartments(Boolean nested) {
        Set<DepartmentResponse> departmentResponses = new HashSet<>();

        if (nested) {
            Set<Long> idSet = new HashSet<>();
            Lists.newArrayList(departmentRepository.findAll()).forEach((Department department) -> {
                if (!idSet.contains(department.getId())) {
                    idSet.add(department.getId());
                    departmentResponses.add(convertFromDepartmentToDepartmentResponse(idSet, department));
                }

            });
        } else {
            Lists.newArrayList(departmentRepository.findAll()).forEach((Department department) -> {
                addToDepartmentResponseSetNoNested(departmentResponses, department);
            });
        }

        return departmentResponses;
    }

    public DepartmentResponse getDepartment(Long id) {
        Department department = departmentRepository.getById(id);
        Set<Long> idSet = new HashSet<>();
        idSet.add(department.getId());
        return convertFromDepartmentToDepartmentResponse(idSet, department);
    }

    public void updateDepartmentById(Long id, DepartmentRequest departmentRequest) {
        if (!departmentRepository.existsById(id)) {
            throw new IllegalStateException("There is no department with that id");
        }

        departmentRepository.setDepartmentById(id,
                departmentRequest.getName(),
                departmentRequest.getDescription());

        manageRepository.save(new Manage(
                id,
                departmentRepository.findById(id).get(),
                employeeRepository.findById(departmentRequest.getManagerOfUnitId()).get()
        ));

        works_inRepository.save(new Works_In(
                departmentRequest.getManagerOfUnitId(),
                employeeRepository.findById(departmentRequest.getManagerOfUnitId()).get(),
                departmentRepository.findById(id).get()
        ));

    }

    @Transactional
    public void insertDepartmentById(DepartmentRequest departmentRequest) {
        if (departmentRepository.findByName(departmentRequest.getName()) == null) {

            Department savedDepartment = departmentRepository.save(new Department(
                    null,
                    null,
                    departmentRequest.getName(),
                    null,
                    null,
                    departmentRequest.getDescription(),
                    null,
                    departmentRepository.findById(departmentRequest.getHeadOfUnitId()).get(),
                    null,
                    null,
                    null
            ));

            manageRepository.save(new Manage(
                    savedDepartment.getId(),
                    savedDepartment,
                    employeeRepository.findById(departmentRequest.getManagerOfUnitId()).get()
            ));

            if (works_inRepository.existsById(departmentRequest.getManagerOfUnitId())) {
                works_inRepository.setDepartmentId(departmentRequest.getManagerOfUnitId(), savedDepartment.getId());
            } else {
                works_inRepository.save(new Works_In(
                        departmentRequest.getManagerOfUnitId(),
                        employeeRepository.findById(departmentRequest.getManagerOfUnitId()).get(),
                        savedDepartment
                ));
            }

        }

    }

    public Department save(Department department) {
        return departmentRepository.save(department);
    }

    public Long count() {
        return departmentRepository.count();
    }

    public void deleteDepartmentById(Long id) {
        departmentRepository.deleteById(id);
    }
}
