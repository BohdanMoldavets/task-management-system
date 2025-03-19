package com.moldavets.task_management_system.employee.controller;

import com.moldavets.task_management_system.employee.dto.RequestEmployeeDto;
import com.moldavets.task_management_system.employee.dto.ResponseEmployeeDto;
import com.moldavets.task_management_system.employee.exception.EmployeeNotFoundException;
import com.moldavets.task_management_system.employee.mapper.EmployeeDtoMapper;
import com.moldavets.task_management_system.employee.model.Employee;
import com.moldavets.task_management_system.employee.service.EmployeeService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<ResponseEmployeeDto>> getAllEmployees() {
        List<ResponseEmployeeDto> responseEmployeeList = new ArrayList<>();
        employeeService.getAllEmployees().forEach(employee -> responseEmployeeList.add(
                        EmployeeDtoMapper.mapToResponseEmployeeDto(employee)
                ));
        return new ResponseEntity<>(responseEmployeeList, HttpStatus.OK);
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<ResponseEmployeeDto> getEmployeeById(@PathVariable("employeeId") Long employeeId) {
        Employee storedEmployee = employeeService.getById(employeeId);
        if(storedEmployee == null) {
            throw new EmployeeNotFoundException(String.format("Employee with id %s not found", employeeId));
        }
        return new ResponseEntity<>(EmployeeDtoMapper.mapToResponseEmployeeDto(storedEmployee), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseEmployeeDto> createEmployee(@Valid @RequestBody RequestEmployeeDto requestEmployeeDto) {
        Employee mappedEmployee = EmployeeDtoMapper.mapRequestEmployeeDto(requestEmployeeDto);
        mappedEmployee.setCreated(new Date());

        Employee storedEmployee = employeeService.save(mappedEmployee);
        return new ResponseEntity<>(EmployeeDtoMapper.mapToResponseEmployeeDto(storedEmployee), HttpStatus.CREATED);
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<ResponseEmployeeDto> updateEmployee(@PathVariable Long employeeId,
                                                              @Valid @RequestBody RequestEmployeeDto requestEmployeeDto) {
        Employee storedEmployee = employeeService.getById(employeeId);
        if(storedEmployee == null) {
            throw new EmployeeNotFoundException(String.format("Employee with id %s not found", employeeId));
        }

        storedEmployee.setUpdated(new Date());
        storedEmployee.setUsername(requestEmployeeDto.getUsername());
        storedEmployee.setEmail(requestEmployeeDto.getEmail());
        storedEmployee.setPassword(requestEmployeeDto.getPassword());

        Employee updatedEmployee = employeeService.save(storedEmployee);

        return new ResponseEntity<>(EmployeeDtoMapper.mapToResponseEmployeeDto(updatedEmployee), HttpStatus.OK);
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<HttpStatusCode> deleteEmployeeById(@PathVariable("employeeId") Long employeeId) {
        employeeService.deleteById(employeeId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
