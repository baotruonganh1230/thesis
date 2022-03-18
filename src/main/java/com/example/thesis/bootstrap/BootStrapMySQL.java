package com.example.thesis.bootstrap;

import com.example.thesis.entities.*;
import com.example.thesis.services.AccountService;
import com.example.thesis.services.DepartmentService;
import com.example.thesis.services.EmployeeService;
import com.example.thesis.services.RoleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;

/**
 * Created by jt on 8/7/17.
 */
@Slf4j
@Component
@Profile({"dev", "prod"})
@AllArgsConstructor
public class BootStrapMySQL implements ApplicationListener<ContextRefreshedEvent> {

    private final AccountService accountService;

    private final RoleService roleService;

    private final EmployeeService employeeService;

    private final DepartmentService departmentService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        if (employeeService.count() == 0L && roleService.count() == 0L && accountService.count() == 0L){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            formatter = formatter.withLocale(Locale.US);

            Employee savedEmployee = employeeService.save(
                    new Employee("Bao",
                            "Truong",
                            "truonganhbao1232@gmail.com",
                            "21 Jump St.",
                            "22 Jump St.",
                            "0854626722",
                            "Male",
                            LocalDate.parse("2021-10-21", formatter)));

            System.out.println(savedEmployee.getId());

            Role savedRole = roleService.save(new Role("USER", "User role"));

            accountService.save(new Account(
                    savedEmployee.getId(),
                    savedRole.getId(),
                    savedEmployee,
                    savedRole,
                    "anhbao1230",
                    ("Abc1230g"),
                    AccountStatus.ENABLE));
        }

        if (departmentService.count() == 0L) {
            Department department1 = new Department("Dev", 2, "sub", "SD", null, null);
            Department department2 = new Department("Tech", 5, "sub-head", "Technology", new HashSet<>(Arrays.asList(department1)), null);
            Department department3 = new Department("Director", 10, "head", "Chairpeople", new HashSet<>(Arrays.asList(department2)), null);
            department1.setHeadOfUnit(department2);
            department2.setHeadOfUnit(department3);
            departmentService.save(department1);
            departmentService.save(department2);
            departmentService.save(department3);
        }

    }

}
