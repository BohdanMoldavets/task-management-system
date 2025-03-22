package com.moldavets.task_management_system.auth.service;

import com.moldavets.task_management_system.auth.dto.JwtRequest;
import com.moldavets.task_management_system.auth.dto.RequestRegistrationEmployeeDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> createAuthToken(JwtRequest authRequest);
    ResponseEntity<?> createNewEmployee(RequestRegistrationEmployeeDto requestRegistrationEmployeeDto);
}