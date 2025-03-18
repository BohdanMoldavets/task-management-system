package com.moldavets.task_management_system.employee.controller;

import com.moldavets.task_management_system.employee.dto.RequestEmployeeDto;
import com.moldavets.task_management_system.employee.dto.ResponseEmployeeDto;
import com.moldavets.task_management_system.employee.mapper.EmployeeDtoMapper;
import com.moldavets.task_management_system.employee.model.Employee;
import com.moldavets.task_management_system.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<ResponseEmployeeDto> getEmployeeById(@PathVariable("employeeId") Long employeeId) {
        Employee storedEmployee = employeeService.getById(employeeId);
        if(storedEmployee == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(EmployeeDtoMapper.mapToResponseEmployeeDto(storedEmployee));
    }

    @PostMapping
    public ResponseEntity<ResponseEmployeeDto> createEmployee(@RequestBody RequestEmployeeDto requestEmployeeDto) {
        Employee storedEmployee = employeeService.save(EmployeeDtoMapper.mapRequestEmployeeDto(requestEmployeeDto));

        return ResponseEntity.ok().body(EmployeeDtoMapper.mapToResponseEmployeeDto(storedEmployee));
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<ResponseEmployeeDto> updateEmployee(@PathVariable Long employeeId,
                                                              @RequestBody RequestEmployeeDto requestEmployeeDto) {
        Employee storedEmployee = employeeService.getById(employeeId);
        if(storedEmployee == null) {
            return ResponseEntity.notFound().build();
        }

        storedEmployee.setUpdated(new Date());
        storedEmployee.setUsername(requestEmployeeDto.getUsername());
        storedEmployee.setEmail(requestEmployeeDto.getEmail());
        storedEmployee.setPassword(requestEmployeeDto.getPassword());

        Employee updatedEmployee = employeeService.save(storedEmployee);

        return ResponseEntity.ok().body(EmployeeDtoMapper.mapToResponseEmployeeDto(updatedEmployee));
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<?> deleteEmployeeById(@PathVariable("employeeId") Long employeeId) {
        if (employeeId == null) {
            return ResponseEntity.badRequest().body(null);
        }
        employeeService.deleteById(employeeId);
        return ResponseEntity.ok().build();
    }
}
