package com.example.thesis.services;

import com.example.thesis.entities.*;
import com.example.thesis.repositories.*;
import com.example.thesis.requests.EmployeeRequest;
import com.example.thesis.responses.Bonus;
import com.example.thesis.responses.EmployeeResponse;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final GoogleDriveService googleDriveService;
    private final AccountService accountService;
    private final Bonus_ListRepository bonus_listRepository;
    private final Works_InRepository works_inRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final InsuranceRepository insuranceRepository;
    private final Insurance_TypeRepository insurance_typeRepository;
    private final RoleRepository roleRepository;

    public List<EmployeeResponse> getEmployees() {
        List<EmployeeResponse> employeeResponses = new ArrayList<>();

        Lists.newArrayList(employeeRepository.findAll()).forEach((Employee employee) -> {
            EmployeeResponse employeeResponse = new EmployeeResponse(
                    employee.getId(),
                    employee.getAvatar(),
                    employee.getFirst_name(),
                    employee.getLast_name(),
                    employee.getEmail(),
                    employee.getPermanent_address(),
                    employee.getTemporary_address(),
                    employee.getPhone(),
                    employee.getWorks_in() == null ? null : employee.getWorks_in().getDepartment().getName(),
                    employee.getPosition() == null ? null : employee.getPosition().getName(),
                    employee.getDate_of_birth(),
                    employee.getPit(),
                    employee.getPosition() == null ? null : employee.getPosition().getSalaryGroup(),
                    employee.getGross_salary(),
                    employee.getBonus_lists().stream().map((Bonus_List bonus_list) -> {
                        return new Bonus(bonus_list.getId(), bonus_list.getName(), bonus_list.getAmount());
                    }).collect(Collectors.toList())
            );

            employeeResponses.add(employeeResponse);
        });

        return employeeResponses;
    }

    public File convertMultiPartFiletoFile(MultipartFile multipartFile) {
        File file = new File(new FileSystemResource("").getFile().getAbsolutePath() + "/src/main/resources/avatars/" + multipartFile.getOriginalFilename());
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public EmployeeResponse getEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new EntityNotFoundException("Cannot find employee with id " + id);
        }
        Employee employee = employeeRepository.getById(id);
        return new EmployeeResponse(
                employee.getId(),
                employee.getAvatar(),
                employee.getFirst_name(),
                employee.getLast_name(),
                employee.getEmail(),
                employee.getPermanent_address(),
                employee.getTemporary_address(),
                employee.getPhone(),
                employee.getWorks_in().getDepartment().getName(),
                employee.getPosition().getName(),
                employee.getDate_of_birth(),
                employee.getPit(),
                employee.getPosition().getSalaryGroup(),
                employee.getGross_salary(),
                employee.getBonus_lists().stream().map((Bonus_List bonus_list) -> {
                    return new Bonus(bonus_list.getId(), bonus_list.getName(), bonus_list.getAmount());
                }).collect(Collectors.toList())
        );
    }

    public void updateEmployeeById(Long id, MultipartFile file, EmployeeRequest employeeRequest) {
        if (!employeeRepository.existsById(id)) {
            throw new IllegalStateException("There is no employee with that id");
        }

        if (employeeRequest.getPersonalDetail() != null) {
            File filetoUpload = convertMultiPartFiletoFile(file);
            Tika tika = new Tika();
            String mimeType = null;
            try {
                mimeType = tika.detect(filetoUpload);
            } catch (IOException e) {
                e.printStackTrace();
            }
            com.google.api.services.drive.model.File upLoadedFile =
                    googleDriveService.upLoadFile(filetoUpload.getName(),
                            filetoUpload.getAbsolutePath(), mimeType);

            if (!filetoUpload.delete()) {
                throw new IllegalStateException("Cannot delete file!");
            }
            employeeRepository.setEmployeePersonalById(id,
                    employeeRequest.getPersonalDetail().getDateOfBirth(),
                    employeeRequest.getPersonalDetail().getEmail(),
                    employeeRequest.getPersonalDetail().getFirstName(),
                    upLoadedFile.getWebContentLink(),
                    employeeRequest.getPersonalDetail().getLastName(),
                    employeeRequest.getPersonalDetail().getPermanentAddress().toString(),
                    employeeRequest.getPersonalDetail().getPhone(),
                    employeeRequest.getPersonalDetail().getSex(),
                    employeeRequest.getPersonalDetail().getTemporaryAddress().toString()
            );

        } else if (employeeRequest.getAccountDetail() != null) {
            if (employeeRequest.getAccountDetail().getType().equalsIgnoreCase("new")) {
                accountService.insertAccount(employeeRequest.getAccountDetail().getNewAccount());
            } else {
                accountService.updateAccountById(employeeRequest.getAccountDetail().getNewAccount().getId(),
                        employeeRequest.getAccountDetail().getNewAccount());
            }
        } else if (employeeRequest.getJobDetail() != null) {
            employeeRepository.setEmployeeJobById(id, employeeRequest.getJobDetail().getJoinDate(),
                    employeeRequest.getJobDetail().getSalary(),
                    employeeRequest.getJobDetail().getPit(),
                    employeeRequest.getJobDetail().getJobId());
            positionRepository.setSalaryGroupById(employeeRequest.getJobDetail().getJobId(),
                    employeeRequest.getJobDetail().getSalaryGroup());
            works_inRepository.save(new Works_In(id, employeeRepository.getById(id),
                    departmentRepository.getById(employeeRequest.getJobDetail().getDepartmentId())));
            for (Bonus bonus : employeeRequest.getJobDetail().getBonus()) {
                Bonus_List bonus_list = new Bonus_List(bonus.getId(), bonus.getBonusName(),
                        bonus.getBonusAmount(),
                        employeeRepository.getById(id));
                bonus_listRepository.save(bonus_list);
            }
        } else {
            Insurance_Type insurance_type =
                    insurance_typeRepository
                            .findByName(employeeRequest.getInsuranceDetail().getInsuranceCommon().getType());
            insuranceRepository.save(
                    new Insurance(
                            employeeRequest.getInsuranceDetail().getId(),
                            id,
                            insurance_type.getId(),
                            employeeRepository.getById(id),
                            insurance_type,
                            employeeRequest.getInsuranceDetail().getInsuranceCommon().getFrom_date(),
                            employeeRequest.getInsuranceDetail().getInsuranceCommon().getTo_date(),
                            employeeRequest.getInsuranceDetail().getInsuranceCommon().getIssue_date(),
                            employeeRequest.getInsuranceDetail().getInsuranceCommon().getNumber(),
                            employeeRequest.getInsuranceDetail().getCityId(),
                            employeeRequest.getInsuranceDetail().getKcbId()));
        }

    }

    @Transactional
    public void insertEmployeeById(MultipartFile file, EmployeeRequest employeeRequest) {
        File filetoUpload = convertMultiPartFiletoFile(file);
        Tika tika = new Tika();
        String mimeType = null;
        try {
            mimeType = tika.detect(filetoUpload);
        } catch (IOException e) {
            e.printStackTrace();
        }
        com.google.api.services.drive.model.File upLoadedFile =
                googleDriveService.upLoadFile(filetoUpload.getName(),
                        filetoUpload.getAbsolutePath(), mimeType);

        if (!filetoUpload.delete()) {
            throw new IllegalStateException("Cannot delete file!");
        }

        Employee savedEmployee = employeeRepository.save(
                new Employee(employeeRequest.getPersonalDetail().getFirstName(),
                        employeeRequest.getPersonalDetail().getLastName(),
                        employeeRequest.getPersonalDetail().getEmail(),
                        employeeRequest.getPersonalDetail().getPermanentAddress().toString(),
                        employeeRequest.getPersonalDetail().getTemporaryAddress().toString(),
                        employeeRequest.getPersonalDetail().getPhone(),
                        employeeRequest.getJobDetail().getSalary(),
                        employeeRequest.getJobDetail().getJoinDate(),
                        employeeRequest.getPersonalDetail().getSex(),
                        employeeRequest.getPersonalDetail().getDateOfBirth(),
                        employeeRequest.getJobDetail().getPit(),
                        upLoadedFile.getWebContentLink(),
                        positionRepository.getById(employeeRequest.getJobDetail().getJobId())
                )
        );

        positionRepository.setSalaryGroupById(employeeRequest.getJobDetail().getJobId(),
                employeeRequest.getJobDetail().getSalaryGroup());
        works_inRepository.save(new Works_In(savedEmployee.getId(), savedEmployee,
                departmentRepository.getById(employeeRequest.getJobDetail().getDepartmentId())));
        for (Bonus bonus : employeeRequest.getJobDetail().getBonus()) {
            Bonus_List bonus_list = new Bonus_List(bonus.getId(), bonus.getBonusName(),
                    bonus.getBonusAmount(),
                    employeeRepository.getById(savedEmployee.getId()));
            bonus_listRepository.save(bonus_list);
        }

        Insurance_Type insurance_type =
                insurance_typeRepository
                        .findByName(employeeRequest.getInsuranceDetail().getInsuranceCommon().getType());
        insuranceRepository.save(
                new Insurance(
                        employeeRequest.getInsuranceDetail().getId(),
                        savedEmployee.getId(),
                        insurance_type.getId(),
                        savedEmployee,
                        insurance_type,
                        employeeRequest.getInsuranceDetail().getInsuranceCommon().getFrom_date(),
                        employeeRequest.getInsuranceDetail().getInsuranceCommon().getTo_date(),
                        employeeRequest.getInsuranceDetail().getInsuranceCommon().getIssue_date(),
                        employeeRequest.getInsuranceDetail().getInsuranceCommon().getNumber(),
                        employeeRequest.getInsuranceDetail().getCityId(),
                        employeeRequest.getInsuranceDetail().getKcbId()));

        if (employeeRequest.getAccountDetail().getType().equalsIgnoreCase("new")) {
            accountService.insertAccount(employeeRequest.getAccountDetail().getNewAccount());
        } else {
            accountService.save(
                    new Account(employeeRequest.getAccountDetail().getNewAccount().getId(),
                            employeeRepository.getById(savedEmployee.getId()),
                            roleRepository.getById(employeeRequest.getAccountDetail().getRoleId()),
                            employeeRequest.getAccountDetail().getNewAccount().getUsername(),
                            employeeRequest.getAccountDetail().getNewAccount().getPassword(),
                            employeeRequest.getAccountDetail().getNewAccount().getStatus()));
        }
    }

    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    public long count() {
        return employeeRepository.count();
    }
}
