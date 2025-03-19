package com.moldavets.task_management_system.employee.service.Impl;

import com.moldavets.task_management_system.employee.dto.RequestEmployeeDto;
import com.moldavets.task_management_system.exception.ResourceNotFoundException;
import com.moldavets.task_management_system.employee.model.Employee;
import com.moldavets.task_management_system.employee.repository.EmployeeRepository;
import com.moldavets.task_management_system.employee.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee getById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Employee with id %s not found", id)));
    }

    @Override
    @Transactional
    public Employee save(Employee employee) {
        employee.setCreated(new Date());
        return employeeRepository.save(employee);
    }

    @Override
    @Transactional
    public Employee update(Long id, RequestEmployeeDto requestEmployeeDto) {
        Employee storedEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Employee with id %s not found", id)));

        storedEmployee.setUpdated(new Date());
        storedEmployee.setUsername(requestEmployeeDto.getUsername());
        storedEmployee.setEmail(requestEmployeeDto.getEmail());
        storedEmployee.setPassword(requestEmployeeDto.getPassword());

        return employeeRepository.save(storedEmployee);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        employeeRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException(String.format("Employee with id %s not found", id)));
        employeeRepository.deleteById(id);
    }
}
