package com.example.thesis.bootstrap;

import com.example.thesis.entities.Account;
import com.example.thesis.entities.AccountStatus;
import com.example.thesis.entities.Employee;
import com.example.thesis.entities.Role;
import com.example.thesis.services.AccountService;
import com.example.thesis.services.EmployeeService;
import com.example.thesis.services.RoleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
                    savedEmployee,
                    savedRole,
                    "anhbao1230",
                    "Abc1230g",
                    AccountStatus.ENABLE));
        }

        execCommand();

    }

    private void execCommand() {
        try {
            Runtime.getRuntime().exec("cd /var/app/current");
            Process process2 = Runtime.getRuntime().exec("jar xf application.jar BOOT-INF/classes/hrms-drive-c2daac40864c.p12");
            process2.waitFor();
            Runtime.getRuntime().exec("cd ~");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
