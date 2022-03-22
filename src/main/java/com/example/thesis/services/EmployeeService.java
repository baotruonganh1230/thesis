package com.example.thesis.services;

import com.example.thesis.entities.*;
import com.example.thesis.repositories.*;
import com.example.thesis.requests.Address;
import com.example.thesis.requests.EmployeeRequest;
import com.example.thesis.responses.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private InsuranceOutputParams getInsuranceOutputParams(Employee employee) {
        InsuranceOutputParams insuranceOutputParams = new InsuranceOutputParams();
        for (Insurance insurance : employee.getInsurances()) {
            if (insurance.getType().getName().equalsIgnoreCase("health")) {
                insuranceOutputParams.setHealth(
                        new InsuranceCommonResponse(
                                insurance.getId(),
                                insurance.getFrom_date(),
                                insurance.getTo_date(),
                                insurance.getIssue_date(),
                                insurance.getNumber()
                        )
                );
                insuranceOutputParams.setCityId(insurance.getCityId());
                insuranceOutputParams.setKcbId(insurance.getKcbId());
            } else if (insurance.getType().getName().equalsIgnoreCase("social")) {
                insuranceOutputParams.setSocial(
                        new InsuranceCommonResponse(
                                insurance.getId(),
                                insurance.getFrom_date(),
                                insurance.getTo_date(),
                                insurance.getIssue_date(),
                                insurance.getNumber()
                        )
                );
            } else {
                insuranceOutputParams.setUnemployment(
                        new InsuranceCommonResponse(
                                insurance.getId(),
                                insurance.getFrom_date(),
                                insurance.getTo_date(),
                                insurance.getIssue_date(),
                                insurance.getNumber()
                        )
                );
            }
        }
        return insuranceOutputParams;
    }


    public List<EmployeeResponse> getEmployees() {
        List<EmployeeResponse> employeeResponses = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        Lists.newArrayList(employeeRepository.findAll()).forEach((Employee employee) -> {
            EmployeeResponse employeeResponse = null;
            try {
                employeeResponse = new EmployeeResponse(
                        employee.getId(),
                        new PersonalDetailOutputParams(
                                employee.getAvatar(),
                                employee.getFirst_name(),
                                employee.getLast_name(),
                                employee.getEmail(),
                                employee.getPhone(),
                                employee.getSex(),
                                employee.getDate_of_birth(),
                                mapper.readValue(employee.getPermanent_address(), Address.class),
                                mapper.readValue(employee.getTemporary_address(), Address.class)
                        ),
                        new JobDetailOutputParams(
                                employee.getEmployed_date(),
                                employee.getPosition() == null ? null : employee.getPosition().getId(),
                                employee.getPit(),
                                employee.getWorks_in() == null ? null : employee.getWorks_in().getDepartment().getId(),
                                employee.getPosition() == null ? null : employee.getPosition().getSalaryGroup(),
                                employee.getGross_salary(),
                                employee.getBonus_lists() == null ? null : employee.getBonus_lists()
                                        .stream()
                                        .map(bonus_list -> new Bonus(
                                                bonus_list.getId(),
                                                bonus_list.getName(),
                                                bonus_list.getAmount()
                                        )).collect(Collectors.toList())
                        ),
                        getInsuranceOutputParams(employee)
                );
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

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
        ObjectMapper mapper = new ObjectMapper();
        Employee employee = employeeRepository.getById(id);

        try {
            return new EmployeeResponse(
                    employee.getId(),
                    new PersonalDetailOutputParams(
                            employee.getAvatar(),
                            employee.getFirst_name(),
                            employee.getLast_name(),
                            employee.getEmail(),
                            employee.getPhone(),
                            employee.getSex(),
                            employee.getDate_of_birth(),
                            mapper.readValue(employee.getPermanent_address(), Address.class),
                            mapper.readValue(employee.getTemporary_address(), Address.class)
                    ),
                    new JobDetailOutputParams(
                            employee.getEmployed_date(),
                            employee.getPosition() == null ? null : employee.getPosition().getId(),
                            employee.getPit(),
                            employee.getWorks_in() == null ? null : employee.getWorks_in().getDepartment().getId(),
                            employee.getPosition() == null ? null : employee.getPosition().getSalaryGroup(),
                            employee.getGross_salary(),
                            employee.getBonus_lists() == null ? null : employee.getBonus_lists()
                                    .stream()
                                    .map(bonus_list -> new Bonus(
                                            bonus_list.getId(),
                                            bonus_list.getName(),
                                            bonus_list.getAmount()
                                    )).collect(Collectors.toList())
                    ),
                    getInsuranceOutputParams(employee)
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
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
                    "https://drive.google.com/thumbnail?authuser=0&sz=w320&id=" + upLoadedFile.getWebContentLink().replace("https://drive.google.com/uc?id=", "").replace("&export=download", ""),
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
                accountService.updateAccountById(employeeRequest.getAccountDetail().getId(),
                        employeeRequest.getAccountDetail().getNewAccount());
            }
        } else if (employeeRequest.getJobDetail() != null) {
            employeeRepository.setEmployeeJobById(id, employeeRequest.getJobDetail().getJoinDate(),
                    employeeRequest.getJobDetail().getSalary(),
                    employeeRequest.getJobDetail().getPit(),
                    employeeRequest.getJobDetail().getJobId());
            positionRepository.setSalaryGroupById(employeeRequest.getJobDetail().getJobId(),
                    employeeRequest.getJobDetail().getSalaryGroup());

            if (works_inRepository.existsById(id)) {
                works_inRepository.setDepartmentId(id, employeeRequest.getJobDetail().getDepartmentId());
            } else {
                works_inRepository.save(new Works_In(
                        id,
                        employeeRepository.findById(id).get(),
                        departmentRepository.findById(employeeRequest.getJobDetail().getDepartmentId()).get()
                ));
            }
            for (Bonus bonus : employeeRequest.getJobDetail().getBonus()) {
                Bonus_List bonus_list = new Bonus_List(bonus.getId(), bonus.getBonusName(),
                        bonus.getBonusAmount(),
                        employeeRepository.getById(id));
                bonus_listRepository.save(bonus_list);
            }
        } else {
            Insurance_Type insurance_typeSocial = insurance_typeRepository.findByName("social");
            insuranceRepository.save(
                    new Insurance(
                            employeeRequest.getInsuranceDetail().getSocial().getId() == null ? null : employeeRequest.getInsuranceDetail().getSocial().getId(),
                            employeeRepository.getById(id),
                            insurance_typeSocial,
                            employeeRequest.getInsuranceDetail().getSocial().getFrom_date(),
                            employeeRequest.getInsuranceDetail().getSocial().getTo_date(),
                            employeeRequest.getInsuranceDetail().getSocial().getIssue_date(),
                            employeeRequest.getInsuranceDetail().getSocial().getNumber(),
                            null,
                            null));

            Insurance_Type insurance_typeUnemployment = insurance_typeRepository.findByName("unemployment");
            insuranceRepository.save(
                    new Insurance(
                            employeeRequest.getInsuranceDetail().getUnemployment().getId() == null ? null : employeeRequest.getInsuranceDetail().getUnemployment().getId(),
                            employeeRepository.getById(id),
                            insurance_typeUnemployment,
                            employeeRequest.getInsuranceDetail().getUnemployment().getFrom_date(),
                            employeeRequest.getInsuranceDetail().getUnemployment().getTo_date(),
                            employeeRequest.getInsuranceDetail().getUnemployment().getIssue_date(),
                            employeeRequest.getInsuranceDetail().getUnemployment().getNumber(),
                            null,
                            null));

            Insurance_Type insurance_typeHealth = insurance_typeRepository.findByName("health");
            insuranceRepository.save(
                    new Insurance(
                            employeeRequest.getInsuranceDetail().getHealth().getId() == null ? null : employeeRequest.getInsuranceDetail().getHealth().getId(),
                            employeeRepository.getById(id),
                            insurance_typeHealth,
                            employeeRequest.getInsuranceDetail().getHealth().getFrom_date(),
                            employeeRequest.getInsuranceDetail().getHealth().getTo_date(),
                            employeeRequest.getInsuranceDetail().getHealth().getIssue_date(),
                            employeeRequest.getInsuranceDetail().getHealth().getNumber(),
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
                        "https://drive.google.com/thumbnail?authuser=0&sz=w320&id=" + upLoadedFile.getWebContentLink().replace("https://drive.google.com/uc?id=", "").replace("&export=download", ""),
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

        Insurance_Type insurance_typeSocial = insurance_typeRepository.findByName("social");
        insuranceRepository.save(
                new Insurance(
                        employeeRequest.getInsuranceDetail().getSocial().getId() == null ? null : employeeRequest.getInsuranceDetail().getSocial().getId(),
                        savedEmployee,
                        insurance_typeSocial,
                        employeeRequest.getInsuranceDetail().getSocial().getFrom_date(),
                        employeeRequest.getInsuranceDetail().getSocial().getTo_date(),
                        employeeRequest.getInsuranceDetail().getSocial().getIssue_date(),
                        employeeRequest.getInsuranceDetail().getSocial().getNumber(),
                        null,
                        null));

        Insurance_Type insurance_typeUnemployment = insurance_typeRepository.findByName("unemployment");
        insuranceRepository.save(
                new Insurance(
                        employeeRequest.getInsuranceDetail().getUnemployment().getId() == null ? null : employeeRequest.getInsuranceDetail().getUnemployment().getId(),
                        savedEmployee,
                        insurance_typeUnemployment,
                        employeeRequest.getInsuranceDetail().getUnemployment().getFrom_date(),
                        employeeRequest.getInsuranceDetail().getUnemployment().getTo_date(),
                        employeeRequest.getInsuranceDetail().getUnemployment().getIssue_date(),
                        employeeRequest.getInsuranceDetail().getUnemployment().getNumber(),
                        null,
                        null));

        Insurance_Type insurance_typeHealth = insurance_typeRepository.findByName("health");
        insuranceRepository.save(
                new Insurance(
                        employeeRequest.getInsuranceDetail().getHealth().getId() == null ? null : employeeRequest.getInsuranceDetail().getHealth().getId(),
                        savedEmployee,
                        insurance_typeHealth,
                        employeeRequest.getInsuranceDetail().getHealth().getFrom_date(),
                        employeeRequest.getInsuranceDetail().getHealth().getTo_date(),
                        employeeRequest.getInsuranceDetail().getHealth().getIssue_date(),
                        employeeRequest.getInsuranceDetail().getHealth().getNumber(),
                        employeeRequest.getInsuranceDetail().getCityId(),
                        employeeRequest.getInsuranceDetail().getKcbId()));

        if (employeeRequest.getAccountDetail() != null) {
            if (employeeRequest.getAccountDetail().getType().equalsIgnoreCase("new")) {
                accountService.insertAccount(employeeRequest.getAccountDetail().getNewAccount());
            } else {
                accountService.save(
                        new Account(employeeRequest.getAccountDetail().getId(),
                                employeeRepository.getById(savedEmployee.getId()),
                                roleRepository.getById(employeeRequest.getAccountDetail().getNewAccount().getRoleid()),
                                employeeRequest.getAccountDetail().getNewAccount().getUsername(),
                                employeeRequest.getAccountDetail().getNewAccount().getPassword(),
                                employeeRequest.getAccountDetail().getNewAccount().getStatus()));
            }
        }
    }

    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    public long count() {
        return employeeRepository.count();
    }
}
