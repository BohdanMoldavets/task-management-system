package com.moldavets.task_management_system.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String ROLE_MANAGER = "ROLE_MANAGER";

    private final UserDetailsService employeeService;
    private final JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(HttpMethod.GET,"/api/v1/employees").authenticated()
                        .requestMatchers(HttpMethod.PUT,"/api/v1/employees/**").hasAuthority(ROLE_MANAGER)
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/employees/**").hasAuthority(ROLE_MANAGER)

                        .requestMatchers(HttpMethod.GET,"/api/v1/tasks/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH,"/api/v1/tasks/**").authenticated()
                        .requestMatchers(HttpMethod.POST,"/api/v1/tasks/**").hasAuthority(ROLE_MANAGER)
                        .requestMatchers(HttpMethod.PUT,"/api/v1/tasks/**").hasAuthority(ROLE_MANAGER)
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/tasks/**").hasAuthority(ROLE_MANAGER)

                        .requestMatchers("/api/v1/auth/login/**").permitAll()
                        .requestMatchers("/api/v1/auth/registration").hasAuthority(ROLE_MANAGER)
                        .anyRequest().authenticated()

                ).sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ).exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                ).addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(employeeService);
        return daoAuthenticationProvider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
