package com.moldavets.task_management_system.auth.contoller;

import com.moldavets.task_management_system.auth.dto.JwtRequest;
import com.moldavets.task_management_system.auth.dto.JwtResponse;
import com.moldavets.task_management_system.auth.dto.RequestRegistrationEmployeeDto;
import com.moldavets.task_management_system.auth.exception.RegistrationException;
import com.moldavets.task_management_system.auth.mapper.AuthMapper;
import com.moldavets.task_management_system.auth.service.AuthService;
import com.moldavets.task_management_system.auth.utils.JwtTokenUtils;
import com.moldavets.task_management_system.employee.service.Impl.EmployeeServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        return authService.createAuthToken(authRequest);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> createNewEmployee(@Valid @RequestBody RequestRegistrationEmployeeDto requestRegistrationEmployeeDto) {
        return authService.createNewEmployee(requestRegistrationEmployeeDto);
    }

}
