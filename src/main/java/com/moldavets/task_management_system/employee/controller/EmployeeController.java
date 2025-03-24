package com.moldavets.task_management_system.employee.controller;

import com.moldavets.task_management_system.employee.dto.RequestEmployeeDto;
import com.moldavets.task_management_system.employee.dto.ResponseEmployeeDto;
import com.moldavets.task_management_system.employee.mapper.EmployeeMapper;
import com.moldavets.task_management_system.employee.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<ResponseEmployeeDto>> getAllEmployees() {
        return new ResponseEntity<>(employeeService.getAll().stream()
                .map(EmployeeMapper::mapToResponseEmployeeDto)
                .toList(),
                HttpStatus.OK);
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<ResponseEmployeeDto> getEmployeeById(@PathVariable("employeeId") Long employeeId) {
        return new ResponseEntity<>(
                EmployeeMapper.mapToResponseEmployeeDto(employeeService.getById(employeeId)),
                HttpStatus.OK
        );
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<ResponseEmployeeDto> updateEmployee(@PathVariable Long employeeId,
                                                              @Valid @RequestBody RequestEmployeeDto requestEmployeeDto) {
        return new ResponseEntity<>(
                EmployeeMapper.mapToResponseEmployeeDto(employeeService.update(employeeId, requestEmployeeDto)),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<HttpStatusCode> deleteEmployeeById(@PathVariable("employeeId") Long employeeId) {
        employeeService.deleteById(employeeId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
