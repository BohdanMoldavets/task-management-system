package com.moldavets.task_management_system.employee.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moldavets.task_management_system.TaskManagementSystemApplication;
import com.moldavets.task_management_system.auth.utils.JwtTokenUtils;
import com.moldavets.task_management_system.employee.dto.RequestEmployeeDto;
import com.moldavets.task_management_system.employee.dto.ResponseEmployeeDto;
import com.moldavets.task_management_system.employee.model.Employee;
import com.moldavets.task_management_system.employee.service.EmployeeService;
import com.moldavets.task_management_system.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TaskManagementSystemApplication.class)
@WebMvcTest(value = EmployeeController.class,
            excludeAutoConfiguration = SecurityAutoConfiguration.class)
class EmployeeControllerIntegrationTest {

    @MockitoBean
    private EmployeeService employeeService;

    @MockitoBean
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Employees can be returned")
    void getAllEmployees_shouldReturnEmployees_whenInputContainsValidRequest() throws Exception {
        Employee employeeJohn = new Employee("john", "123", "john@mail.com", null, null);
        Employee employeeJack = new Employee("jack", "123", "jack@mail.com", null, null);

        String employeesJson = objectMapper.writeValueAsString(List.of(employeeJohn, employeeJack));


        when(employeeService.getAll()).thenReturn(
                List.of(
                        employeeJohn,
                        employeeJack
                )
        );

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employees"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(employeesJson));

        verify(employeeService, times(1)).getAll();
    }

    @Test
    @DisplayName("Employee can be returned by id")
    void getEmployeeById_shouldReturnEmployeeById_whenInputContainsValidRequest() throws Exception {
        Employee employee = new Employee("john", "123", "john@mail.com", null, null);
        employee.setId(1L);

        when(employeeService.getById(employee.getId())).thenReturn(employee);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employees/" + employee.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(employee.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(employee.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(employee.getEmail()));

        verify(employeeService, times(1)).getById(anyLong());
    }

    @Test
    @DisplayName("Method should throw exception when employee can not be found")
    void getEmployeeById_shouldThrowException_whenEmployeeNotFound() throws Exception {
        Long employeeId = 1L;
        when(employeeService.getById(anyLong()))
                .thenThrow(new ResourceNotFoundException(String.format("Employee with id %s not found", employeeId)));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employees/" + employeeId))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Employee with id " + employeeId + " not found"));

        verify(employeeService, times(1)).getById(anyLong());
    }

    @Test
    @DisplayName("Employee can be updated")
    void updateEmployee_shouldUpdateEmployee_whenInputContainsValidRequest() throws Exception {
        Employee employee = new Employee("john", "123", "john@mail.com", null, null);
        employee.setId(1L);

        RequestEmployeeDto requestEmployeeDto = new RequestEmployeeDto("jack", "jack@mail.com", "456");

        Employee updatedEmployee = new Employee("jack", "456", "jack@mail.com", null, null);
        updatedEmployee.setId(1L);

        when(employeeService.update(anyLong(), any(RequestEmployeeDto.class))).thenReturn(updatedEmployee);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/employees/" + employee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestEmployeeDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(updatedEmployee.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(updatedEmployee.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(updatedEmployee.getEmail()));

        verify(employeeService, times(1)).update(anyLong(), any(RequestEmployeeDto.class));
    }

    @Test
    @DisplayName("Method should throw exception when employee can not be found")
    void updateEmployee_shouldThrowException_whenEmployeeNotFound() throws Exception {
        Long employeeId = 1L;
        RequestEmployeeDto requestEmployeeDto = new RequestEmployeeDto("test", "test@mail.com", "456");

        when(employeeService.update(anyLong(),any(RequestEmployeeDto.class)))
                .thenThrow(new ResourceNotFoundException(String.format("Employee with id %s not found", employeeId)));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/employees/" + employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestEmployeeDto)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Employee with id " + employeeId + " not found"));

        verify(employeeService, times(1)).update(anyLong(), any(RequestEmployeeDto.class));
    }

    @Test
    @DisplayName("Employee can be deleted")
    void deleteEmployee_shouldDeleteEmployee_whenInputContainsValidRequest() throws Exception {
        doNothing().when(employeeService).deleteById(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/employees/" + 1L))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(employeeService, times(1)).deleteById(anyLong());
    }
}