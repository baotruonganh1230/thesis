package com.example.thesis.services;

import com.example.thesis.entities.*;
import com.example.thesis.repositories.*;
import com.example.thesis.requests.DepartmentRequest;
import com.example.thesis.responses.DepartmentResponse;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final ManageRepository manageRepository;
    private final EmployeeRepository employeeRepository;
    private final Works_InRepository works_inRepository;
    private final Job_RecruitmentRepository job_recruitmentRepository;
    private final HasRepository hasRepository;

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
                    (department.getManage() == null) ? null : department.getManage().getEmployee().getId(),
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
                        (department.getManage() == null) ? null : department.getManage().getEmployee().getId(),
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
            if (departmentResponse.getId().equals(id)) {
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
            Lists.newArrayList(departmentRepository.findAll()).forEach((Department department) ->
                    addToDepartmentResponseSetNoNested(departmentResponses, department));
        }

        return departmentResponses;
    }

    public DepartmentResponse getDepartment(Long id) {
        Department department = departmentRepository.getById(id);
        Set<Long> idSet = new HashSet<>();
        idSet.add(department.getId());
        return convertFromDepartmentToDepartmentResponse(idSet, department);
    }

    @Transactional
    public void updateDepartmentById(Long id, DepartmentRequest departmentRequest) {
        if (!departmentRepository.existsById(id)) {
            throw new IllegalStateException("There is no department with that id");
        }

        departmentRepository.setDepartmentById(id,
                departmentRequest.getName(),
                departmentRequest.getDescription());

        if (manageRepository.existsById(id)) {
            Manage manage = manageRepository.getManageByDid(id);
            if (!manage.getEmployee().getId().equals(departmentRequest.getManagerOfUnitId())) {
                departmentRepository.decreasePeopleCount(manage.getDepartment().getId());
                manageRepository.setEmployeeId(id, departmentRequest.getManagerOfUnitId());
                departmentRepository.increasePeopleCount(id);
            }
        } else {
            manageRepository.save(new Manage(
                    id,
                    departmentRepository.findById(id).get(),
                    employeeRepository.findById(departmentRequest.getManagerOfUnitId()).get()
            ));
        }

        if (works_inRepository.existsById(departmentRequest.getManagerOfUnitId())) {
            Works_In works_in = works_inRepository.getById(departmentRequest.getManagerOfUnitId());
            if (!works_in.getDepartment().getId().equals(id)) {
                departmentRepository.decreasePeopleCount(works_in.getDepartment().getId());
                works_inRepository.setDepartmentId(departmentRequest.getManagerOfUnitId(), id);
                departmentRepository.increasePeopleCount(id);
            }
        } else {
            works_inRepository.save(new Works_In(
                    departmentRequest.getManagerOfUnitId(),
                    employeeRepository.findById(departmentRequest.getManagerOfUnitId()).get(),
                    departmentRepository.findById(id).get()
            ));
        }

    }

    @Transactional
    public void insertDepartmentById(DepartmentRequest departmentRequest) {
        if (departmentRepository.findByName(departmentRequest.getName()) == null) {

            Department savedDepartment = departmentRepository.save(new Department(
                    null,
                    null,
                    departmentRequest.getName(),
                    departmentRequest.getManagerOfUnitId() != null ? 1 : 0,
                    null,
                    departmentRequest.getDescription(),
                    null,
                    departmentRepository.findById(departmentRequest.getHeadOfUnitId()).get(),
                    null,
                    null,
                    null
            ));

            if (manageRepository.existsById(savedDepartment.getId())) {
                manageRepository.setEmployeeId(savedDepartment.getId(), departmentRequest.getManagerOfUnitId());
            } else {
                Employee employee = employeeRepository.getById(departmentRequest.getManagerOfUnitId());

                if (manageRepository.existsByEmployee(employee)) {
                    Manage manage = manageRepository.getManageByEid(departmentRequest.getManagerOfUnitId());
                    departmentRepository.decreasePeopleCount(manage.getDepartment().getId());
                    departmentRepository.increasePeopleCount(savedDepartment.getId());
                    manageRepository.deleteByEmployee(employee);
                }

                manageRepository.save(new Manage(
                        savedDepartment.getId(),
                        savedDepartment,
                        employeeRepository.findById(departmentRequest.getManagerOfUnitId()).get()
                ));
            }

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

    public void deleteDepartmentById(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new EntityNotFoundException("Department does not exist");
        }
        Department department = departmentRepository.getById(id);
        Department headOfUnit = department.getHeadOfUnit();

        for (Job_Recruitment job_recruitment : department.getJob_recruitments()) {
            job_recruitmentRepository.updateToBiggerDepartmentByDid(
                    job_recruitment.getId(),
                    headOfUnit.getId());
        }

        manageRepository.deleteByDid(id);

        for (Works_In works_in : department.getWorks_ins()) {
            works_inRepository.setDepartmentId(works_in.getEid(), headOfUnit.getId());
        }

        for (Department subDepartment : department.getSubUnits()) {
            departmentRepository.setHeadOfUnit(subDepartment.getId(), headOfUnit.getId());
        }

        departmentRepository.deleteById(id);

    }

    public List<Long> getAllSubDepartmentIdsIncludeThis(Long departmentId) {
        Department department = departmentRepository.getById(departmentId);
        List<Long> subDepartmentIds =
                departmentRepository.findAllByHeadOfUnit(department)
                        .stream()
                        .map(
                                Department::getId
                        ).collect(Collectors.toList());
        List<Long> returnListIds = new ArrayList<>();

        for (Long id : subDepartmentIds) {
            List<Long> subOfSubDepartmentIds = getAllSubDepartmentIdsIncludeThis(id);
            if (subOfSubDepartmentIds != null) {
                returnListIds.addAll(subOfSubDepartmentIds);
            }
        }

        returnListIds.add(departmentId);

        return returnListIds;
    }

    public List<Department> getAllSubDepartmentsIncludeThis(Department department) {
        List<Department> subDepartments =
                departmentRepository.findAllByHeadOfUnit(department);
        List<Department> returnList = new ArrayList<>();

        for (Department subDepartment : subDepartments) {
            List<Department> subOfSubDepartments = getAllSubDepartmentsIncludeThis(subDepartment);
            if (subOfSubDepartments != null) {
                returnList.addAll(subOfSubDepartments);
            }
        }

        returnList.add(department);

        return returnList;
    }

}
