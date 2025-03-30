package com.moldavets.task_management_system.auth.contoller;

import com.moldavets.task_management_system.auth.dto.JwtRequest;
import com.moldavets.task_management_system.auth.dto.RequestRegistrationEmployeeDto;
import com.moldavets.task_management_system.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "AuthController")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Sign in by login & password")
    @PostMapping("/login")
    public ResponseEntity<?> createAuthToken(@Valid @RequestBody JwtRequest authRequest) {
        return authService.createAuthToken(authRequest);
    }

    @Operation(summary = "Registration new employee to system")
    @PostMapping("/registration")
    public ResponseEntity<?> createNewEmployee(@Valid @RequestBody RequestRegistrationEmployeeDto requestRegistrationEmployeeDto) {
        return authService.createNewEmployee(requestRegistrationEmployeeDto);
    }

}
