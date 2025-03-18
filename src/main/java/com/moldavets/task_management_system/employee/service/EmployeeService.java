package com.moldavets.task_management_system.employee.service;

import com.moldavets.task_management_system.employee.model.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> getAllEmployees();
    Employee getById(Long id);
    Employee save(Employee employee);
    void deleteById(Long id);
}
