package com.moldavets.task_management_system.employee.mapper;

import com.moldavets.task_management_system.employee.dto.RequestEmployeeDto;
import com.moldavets.task_management_system.employee.dto.ResponseEmployeeDto;
import com.moldavets.task_management_system.employee.model.Employee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeMapperTest {

    @Test
    @DisplayName("Should convert RequestEmployeeDto to Employee")
    void mapRequestEmployeeDto_shouldReturnEmployee_whenInputContainsCorrectRequestEmployeeDto() {
        RequestEmployeeDto requestEmployeeDto = new RequestEmployeeDto("test", "test@mail.com", "123");

        Employee expected = new Employee("test", "123", "test@mail.com", null, null);
        Employee actual = EmployeeMapper.mapRequestEmployeeDto(requestEmployeeDto);

        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getPassword(), actual.getPassword());
    }

    @Test
    @DisplayName("Should convert employee to ResponseEmployeeDto")
    void mapToResponseEmployeeDto_shouldReturnResponseEmployeeDto_whenInputContainsCorrectEmployee() {
        Employee employee = new Employee("test", "123", "test@mail.com", null, null);
        employee.setId(1L);

        ResponseEmployeeDto expected = new ResponseEmployeeDto(1L, "test", "test@mail.com", null, null, null, null);
        ResponseEmployeeDto actual = EmployeeMapper.mapToResponseEmployeeDto(employee);

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getEmail(), actual.getEmail());
    }


//                    .id(employee.getId())
//            .username(employee.getUsername())
//            .email(employee.getEmail())
//            .created(employee.getCreated())
//            .updated(employee.getUpdated())
//            .roles(employee.getRoles())
//            .tasks(employee.getTasks())
//            .build();

}