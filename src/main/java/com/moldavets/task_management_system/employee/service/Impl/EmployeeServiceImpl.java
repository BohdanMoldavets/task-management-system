package com.moldavets.task_management_system.employee.service.Impl;

import com.moldavets.task_management_system.employee.model.Employee;
import com.moldavets.task_management_system.employee.repository.EmployeeRepository;
import com.moldavets.task_management_system.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee getById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void save(Employee employee) {
        employee.setCreated(new Date());
        employeeRepository.save(employee);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if(employeeRepository.findById(id).orElse(null) != null) {
            employeeRepository.deleteById(id);
        }
    }
}
