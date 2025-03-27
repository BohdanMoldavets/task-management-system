package com.moldavets.task_management_system.auth.service.Impl;

import com.moldavets.task_management_system.auth.dto.JwtRequest;
import com.moldavets.task_management_system.auth.dto.JwtResponse;
import com.moldavets.task_management_system.auth.dto.RequestRegistrationEmployeeDto;
import com.moldavets.task_management_system.auth.exception.RegistrationException;
import com.moldavets.task_management_system.auth.exception.UnauthorizedException;
import com.moldavets.task_management_system.auth.mapper.AuthMapper;
import com.moldavets.task_management_system.auth.service.AuthService;
import com.moldavets.task_management_system.auth.utils.JwtTokenUtils;
import com.moldavets.task_management_system.employee.model.Employee;
import com.moldavets.task_management_system.employee.service.Impl.EmployeeServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final EmployeeServiceImpl employeeService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    @Override
    public ResponseEntity<?> createAuthToken(JwtRequest authRequest) {
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

    @Override
    public ResponseEntity<?> createNewEmployee(RequestRegistrationEmployeeDto requestRegistrationEmployeeDto) {
        if (!requestRegistrationEmployeeDto.getPassword().equals(requestRegistrationEmployeeDto.getConfirmPassword())) {
            throw new RegistrationException("Passwords do not match");
        }

        if(employeeService.isExistByUsername(requestRegistrationEmployeeDto.getUsername())) {
            throw new RegistrationException("User with this username already exists");
        }

        Employee registeredEmployee =
                employeeService.save(AuthMapper.mapRegistrationEmployeeDto(requestRegistrationEmployeeDto));

        return new ResponseEntity<>(AuthMapper.mapToResponseRegistrationEmployeeDto(registeredEmployee), HttpStatus.CREATED);
    }

}
