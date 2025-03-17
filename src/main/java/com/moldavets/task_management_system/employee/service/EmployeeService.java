package com.moldavets.task_management_system.employee.service;

import com.moldavets.task_management_system.employee.model.Employee;

public interface EmployeeService {
    Employee getById(Long id);
    void save(Employee employee);
}
