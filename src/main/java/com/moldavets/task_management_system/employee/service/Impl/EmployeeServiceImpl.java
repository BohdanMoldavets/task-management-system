package com.moldavets.task_management_system.employee.service.Impl;

import com.moldavets.task_management_system.employee.model.Employee;
import com.moldavets.task_management_system.employee.repository.EmployeeRepository;
import com.moldavets.task_management_system.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee getById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    @Override
    public void save(Employee employee) {

    }
}
