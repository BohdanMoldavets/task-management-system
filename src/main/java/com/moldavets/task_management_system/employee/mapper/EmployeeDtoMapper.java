package com.moldavets.task_management_system.employee.mapper;

import com.moldavets.task_management_system.employee.dto.RequestEmployeeDto;
import com.moldavets.task_management_system.employee.dto.ResponseEmployeeDto;
import com.moldavets.task_management_system.employee.model.Employee;

public class EmployeeDtoMapper {

    public static Employee mapRequestEmployeeDto(RequestEmployeeDto requestEmployeeDto) {
        return Employee.builder()
                .username(requestEmployeeDto.getUsername())
                .email(requestEmployeeDto.getEmail())
                .password(requestEmployeeDto.getPassword())
                .build();
    }

    public static ResponseEmployeeDto mapToResponseEmployeeDto(Employee employee) {
        return ResponseEmployeeDto.builder()
                .id(employee.getId())
                .username(employee.getUsername())
                .email(employee.getEmail())
                .created(employee.getCreated())
                .updated(employee.getUpdated())
                .roles(employee.getRoles())
                .tasks(employee.getTasks())
                .build();
    }

}
