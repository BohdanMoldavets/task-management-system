package com.moldavets.task_management_system.auth.mapper;

import com.moldavets.task_management_system.auth.dto.RequestRegistrationEmployeeDto;
import com.moldavets.task_management_system.auth.dto.ResponseRegistrationDto;
import com.moldavets.task_management_system.employee.model.Employee;

public class AuthMapper {

    private AuthMapper() {
    }

    public static Employee mapRegistrationEmployeeDto(RequestRegistrationEmployeeDto requestRegistrationEmployeeDto) {
        return Employee.builder()
                .username(requestRegistrationEmployeeDto.getUsername())
                .email(requestRegistrationEmployeeDto.getEmail())
                .password(requestRegistrationEmployeeDto.getPassword())
                .build();
    }

    public static ResponseRegistrationDto mapToResponseRegistrationEmployeeDto(Employee employee) {
        return new ResponseRegistrationDto(
                employee.getId(),
                employee.getUsername(),
                employee.getEmail()
        );
    }

}
