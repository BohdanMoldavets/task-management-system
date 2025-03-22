package com.moldavets.task_management_system.auth.mapper;

import com.moldavets.task_management_system.auth.dto.RegistrationEmployeeRequestDto;
import com.moldavets.task_management_system.auth.dto.RegistrationResponseDto;
import com.moldavets.task_management_system.employee.model.Employee;

public class AuthMapper {

    public static Employee mapRegistrationEmployeeDto(RegistrationEmployeeRequestDto registrationEmployeeRequestDto) {
        return Employee.builder()
                .username(registrationEmployeeRequestDto.getUsername())
                .email(registrationEmployeeRequestDto.getEmail())
                .password(registrationEmployeeRequestDto.getPassword())
                .build();
    }

    public static RegistrationResponseDto mapToRegistrationEmployeeResponseDto(Employee employee) {
        return new RegistrationResponseDto(
                employee.getId(),
                employee.getUsername(),
                employee.getEmail()
        );
    }

}
