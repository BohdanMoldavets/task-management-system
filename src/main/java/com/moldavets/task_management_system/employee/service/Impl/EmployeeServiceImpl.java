package com.moldavets.task_management_system.employee.service.Impl;

import com.moldavets.task_management_system.auth.service.RoleService;
import com.moldavets.task_management_system.employee.dto.RequestEmployeeDto;
import com.moldavets.task_management_system.exception.ResourceNotFoundException;
import com.moldavets.task_management_system.employee.model.Employee;
import com.moldavets.task_management_system.employee.repository.EmployeeRepository;
import com.moldavets.task_management_system.employee.service.EmployeeService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class EmployeeServiceImpl implements EmployeeService, UserDetailsService {

    private final EmployeeRepository employeeRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

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
    public Employee getByUsername(String username) {
        return employeeRepository.findEmployeeByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Employee with username %s not found", username)));
    }

    @Override
    public Boolean isExist(String username) {
        return employeeRepository.existsEmployeeByUsername(username);
    }

    //TODO - created TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() add to migration
    @Override
    @Transactional
    public Employee save(Employee employee) {
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        employee.setCreated(new Date());
        employee.setRoles(List.of(roleService.getByName("ROLE_EMPLOYEE")));
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

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee storedEmployee = getByUsername(username);
        return new User(
                storedEmployee.getUsername(),
                storedEmployee.getPassword(),
                storedEmployee.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList()
        );
    }


}
