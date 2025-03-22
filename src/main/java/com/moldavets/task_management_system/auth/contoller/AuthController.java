package com.moldavets.task_management_system.auth.contoller;

import com.moldavets.task_management_system.auth.dto.JwtRequest;
import com.moldavets.task_management_system.auth.dto.JwtResponse;
import com.moldavets.task_management_system.auth.dto.RegistrationEmployeeRequestDto;
import com.moldavets.task_management_system.auth.exception.RegistrationException;
import com.moldavets.task_management_system.auth.mapper.AuthMapper;
import com.moldavets.task_management_system.auth.utils.JwtTokenUtils;
import com.moldavets.task_management_system.employee.model.Employee;
import com.moldavets.task_management_system.employee.service.Impl.EmployeeServiceImpl;
import com.moldavets.task_management_system.auth.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {

    private final EmployeeServiceImpl employeeService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;


    @PostMapping("/login")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Invalid login/password");
        }
        UserDetails userDetails = employeeService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtils.generateToken(userDetails);
        return new ResponseEntity<>(new JwtResponse(token), HttpStatus.OK);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> createNewEmployee(@RequestBody RegistrationEmployeeRequestDto registrationEmployeeRequestDto) {
        if (!registrationEmployeeRequestDto.getPassword().equals(registrationEmployeeRequestDto.getConfirmPassword())) {
            throw new RegistrationException("Passwords do not match");
        }

        if(employeeService.getByUsername(registrationEmployeeRequestDto.getUsername()) != null){
            throw new RegistrationException("User with this username already exists");
        }

        Employee registeredEmployee =
                employeeService.save(AuthMapper.mapRegistrationEmployeeDto(registrationEmployeeRequestDto));

        return new ResponseEntity<>(AuthMapper.mapToRegistrationEmployeeResponseDto(registeredEmployee), HttpStatus.CREATED);
    }

}
