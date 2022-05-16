package com.example.thesis.config;

import com.example.thesis.services.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private AccountService accountService;

    private JWTTokenHelper jwtTokenHelper;

    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username, password, " +
                        "case " +
                        "    when status = 'enable' " +
                        "    then 1 " +
                        "    when status = 'disable' " +
                        "    then 0 " +
                        "    else null " +
                        "end " +
                        "as enabled " +
                        "from hrmsdev.account where username = ?")
                .authoritiesByUsernameQuery("select a.username, " +
                        "b.name as authority from hrmsdev.account a " +
                        "left join hrmsdev.role b " +
                        "on a.roleid = b.id " +
                        "where username = ?");

        auth.userDetailsService(accountService).passwordEncoder(passwordEncoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.cors().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint).and()
                .authorizeRequests((request) -> request.antMatchers("/api/v1/auth/login").permitAll()
                        .antMatchers("/api/v1/auth/mobile/login").permitAll()
                        .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .antMatchers("/api/unauthorized/**").permitAll()
                        .antMatchers("/api/v1/accounts").hasAnyRole("ADMIN", "SUPERUSER", "USER")
                        .antMatchers(HttpMethod.GET,"/api/v1/account/**").hasAnyRole("ADMIN", "SUPERUSER", "USER")
                        .antMatchers(HttpMethod.PUT,"/api/v1/account/**").hasAnyRole("ADMIN", "SUPERUSER")
                        .antMatchers(HttpMethod.POST,"/api/v1/account").hasAnyRole("ADMIN", "SUPERUSER")
                        .antMatchers("/api/v1/attendances").hasAnyRole("ADMIN", "SUPERUSER", "USER")
                        .antMatchers("/api/v1/checkin_in").hasAnyRole("ADMIN", "SUPERUSER", "USER")
                        .antMatchers("/api/v1/checkin_out").hasAnyRole("ADMIN", "SUPERUSER", "USER")
                        .antMatchers("/api/v1/haveCheckedin").hasAnyRole("ADMIN", "SUPERUSER", "USER")
                        .antMatchers("/api/v1/departments").hasAnyRole("ADMIN", "SUPERUSER", "USER")
                        .antMatchers(HttpMethod.GET,"/api/v1/department/**").hasAnyRole("ADMIN", "SUPERUSER", "USER")
                        .antMatchers(HttpMethod.PUT,"/api/v1/department/**").hasAnyRole("ADMIN", "SUPERUSER")
                        .antMatchers(HttpMethod.POST,"/api/v1/department").hasAnyRole("ADMIN", "SUPERUSER")
                        .antMatchers(HttpMethod.DELETE,"/api/v1/department/**").hasAnyRole("ADMIN", "SUPERUSER")
                        .antMatchers("/api/v1/employees").hasAnyRole("ADMIN", "SUPERUSER", "USER")
                        .antMatchers(HttpMethod.GET,"/api/v1/employee/**").hasAnyRole("ADMIN", "SUPERUSER", "USER")
                        .antMatchers(HttpMethod.PUT,"/api/v1/employee/**").hasAnyRole("ADMIN", "SUPERUSER")
                        .antMatchers(HttpMethod.POST,"/api/v1/employee").hasAnyRole("ADMIN", "SUPERUSER")
                        .antMatchers("/api/v1/getEvents").hasAnyRole("ADMIN", "SUPERUSER", "USER")
                        .antMatchers("/api/v1/updateEvent").hasAnyRole("ADMIN", "SUPERUSER")
                        .antMatchers("/api/v1/sendEvent").hasAnyRole("ADMIN", "SUPERUSER")
                        .antMatchers("/api/v1/job_recruitments").hasAnyRole("ADMIN", "SUPERUSER")
                        .antMatchers(HttpMethod.PUT,"/api/v1/job_recruitment/**").hasAnyRole("ADMIN", "SUPERUSER")
                        .antMatchers(HttpMethod.POST,"/api/v1/job_recruitment").hasAnyRole("ADMIN", "SUPERUSER")
                        .antMatchers(HttpMethod.GET,"/api/v1/leaves").hasAnyRole("ADMIN", "SUPERUSER", "USER")
                        .antMatchers(HttpMethod.POST,"/api/v1/leaves/status").hasAnyRole("ADMIN", "SUPERUSER", "USER")
                        .antMatchers("/api/v1/sendRequestLeave").hasAnyRole("ADMIN", "SUPERUSER", "USER")
                        .antMatchers("/api/v1/getLeave").hasAnyRole("ADMIN", "SUPERUSER", "USER")
                        .antMatchers("/api/v1/notiexpotokens").hasAnyRole("ADMIN", "SUPERUSER", "USER")
                        .antMatchers("/api/v1/payment").hasAnyRole("ADMIN", "SUPERUSER", "USER")
                        .antMatchers("/api/v1/positions").hasAnyRole("ADMIN", "SUPERUSER", "USER")
                        .antMatchers(HttpMethod.GET,"/api/v1/position/**").hasAnyRole("ADMIN", "SUPERUSER", "USER")
                        .antMatchers(HttpMethod.PUT,"/api/v1/position/**").hasAnyRole("ADMIN", "SUPERUSER")
                        .antMatchers(HttpMethod.POST,"/api/v1/position").hasAnyRole("ADMIN", "SUPERUSER")
                        .antMatchers("/api/v1/chart/attendHour").hasAnyRole("ADMIN", "SUPERUSER", "USER")
                        .antMatchers("/api/v1/chart/attendNumber").hasAnyRole("ADMIN", "SUPERUSER", "USER")
                        .antMatchers("/api/v1/link").hasAnyRole("ADMIN", "SUPERUSER", "USER")
                        .anyRequest().authenticated())
                .addFilterBefore(new JWTAuthenticationFilter(accountService, jwtTokenHelper),
                        UsernamePasswordAuthenticationFilter.class);

        http.csrf().disable();
        http.headers().frameOptions().disable();
    }

}
