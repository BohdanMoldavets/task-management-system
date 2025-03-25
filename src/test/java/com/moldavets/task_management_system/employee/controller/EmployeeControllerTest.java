package com.moldavets.task_management_system.employee.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.moldavets.task_management_system.auth.config.JwtRequestFilter;
import com.moldavets.task_management_system.auth.model.Role;
import com.moldavets.task_management_system.employee.dto.RequestEmployeeDto;
import com.moldavets.task_management_system.employee.dto.ResponseEmployeeDto;
import com.moldavets.task_management_system.employee.model.Employee;
import com.moldavets.task_management_system.employee.service.EmployeeService;
import com.moldavets.task_management_system.exception.ResourceNotFoundException;
import com.moldavets.task_management_system.task.model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;


    @Test
    @DisplayName("Employees can be returned")
    void getAllEmployees_shouldReturnAllEmployees_whenInputContainsCorrectRequest() {

        when(employeeService.getAll()).thenReturn(
                List.of(
                        new Employee("John", "password", "email", null, null),
                        new Employee("Jack", "password", "jackmail", null, null)
                )
        );

        ResponseEntity<List<ResponseEmployeeDto>> expected = new ResponseEntity<>(List.of(
                new ResponseEmployeeDto(null, "John", "email", null, null, null, null),
                new ResponseEmployeeDto(null, "Jack", "jackmail", null, null, null, null)
        ), HttpStatus.OK);

        ResponseEntity<List<ResponseEmployeeDto>> actual = employeeController.getAllEmployees();

        assertEquals(expected.getStatusCode(), actual.getStatusCode());
        assertEquals(expected.getBody(), actual.getBody());

    }

    @Test
    @DisplayName("Employee can be returned by id")
    void getEmployeeById_shouldReturnEmployeeById_whenInputContainsCorrectId() {
        when(employeeService.getById(anyLong())).thenReturn(new Employee("John", "password", "email", null, null));

        ResponseEntity<ResponseEmployeeDto> expected = new ResponseEntity<>(
                new ResponseEmployeeDto(null, "John", "email", null, null, null, null),
                HttpStatus.OK
        );

        ResponseEntity<ResponseEmployeeDto> actual = employeeController.getEmployeeById(1L);

        assertEquals(expected.getStatusCode(), actual.getStatusCode());
        assertEquals(expected.getBody(), actual.getBody());
    }

    @Test
    @DisplayName("Method should throw exception when input contains not exist employee")
    void getEmployeeById_shouldThrowException_whenInputContainsNotExistEmployeeId() {
        Long employeeId = 1L;
        when(employeeService.getById(employeeId)).thenThrow(new ResourceNotFoundException(String.format("Employee with id %s not found", employeeId)));

        String expected = "Employee with id " + employeeId + " not found";

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> employeeController.getEmployeeById(employeeId));

        assertEquals(expected, exception.getMessage());
    }

    @Test
    @DisplayName("Method should throw exception when input contains null")
    void getEmployeeById_shouldThrowException_whenInputContainsNull() {
        when(employeeService.getById(null)).thenThrow(new NullPointerException("Id can not be null"));

        String expected = "Id can not be null";

        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> employeeController.getEmployeeById(null));

        assertEquals(expected, exception.getMessage());
    }

    @Test
    @DisplayName("An employee can be updated")
    void updateEmployee_shouldUpdateEmployee_whenInputContainsCorrectRequest() {
        Long employeeId = 1L;
        RequestEmployeeDto requestEmployeeDto = new RequestEmployeeDto("John", "john@mail.com", "123");

        when(employeeService.update(anyLong(), any(RequestEmployeeDto.class)))
                .thenReturn(new Employee("John", "123", "john@mail.com", null, null));

        ResponseEntity<ResponseEmployeeDto> expected = new ResponseEntity<>(new ResponseEmployeeDto(null, "John", "john@mail.com", null, null, null, null), HttpStatus.OK);
        ResponseEntity<ResponseEmployeeDto> actual = employeeController.updateEmployee(employeeId, requestEmployeeDto);

        assertEquals(expected.getStatusCode(), actual.getStatusCode());
        assertEquals(expected.getBody(), actual.getBody());
    }

    @Test
    @DisplayName("Method should throw exception when employee can not be found")
    void updateEmployee_shouldThrowException_whenEmployeeNotFound() {
        Long employeeId = 1L;
        when(employeeService.update(anyLong(), any(RequestEmployeeDto.class)))
                .thenThrow(new ResourceNotFoundException(String.format("Employee with id %s not found", employeeId)));

        String expected = "Employee with id " + employeeId + " not found";

        ResourceNotFoundException actual = assertThrows(ResourceNotFoundException.class,
                () -> employeeService.update(employeeId, new RequestEmployeeDto()));

        assertEquals(expected, actual.getMessage());
    }



    @Test
    @DisplayName("Method should throw exception when employee and request employee dto id is null")
    void updateEmployee_shouldThrowException_whenEmployeeIdIsNull() {
        when(employeeService.update(null, null))
                .thenThrow(new NullPointerException("Id and requestEmployeeDto can not be null"));

        String expected = "Id and requestEmployeeDto can not be null";

        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> employeeController.updateEmployee(null, null));

        assertEquals(expected, exception.getMessage());
    }



//        this.id = id;
//        this.username = username;
//        this.email = email;
//        this.created = created;
//        this.updated = updated;
//        this.roles = roles;
//        this.tasks = tasks;
}