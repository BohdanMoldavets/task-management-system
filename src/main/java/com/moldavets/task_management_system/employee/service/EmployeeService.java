package com.moldavets.task_management_system.employee.service;

import com.moldavets.task_management_system.employee.dto.RequestEmployeeDto;
import com.moldavets.task_management_system.employee.model.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> getAll();
    Employee getById(Long id);
    Employee getByUsername(String username);
    Employee save(Employee employee);
    Employee update(Long id, RequestEmployeeDto requestEmployeeDto);
    void deleteById(Long id);
}
