package com.moldavets.task_management_system.employee.repository;

import com.moldavets.task_management_system.employee.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findEmployeeByUsername(String username);
    Boolean existsEmployeeByUsername(String username);
    void deleteEmployeeById(Long id);
}
