package com.moldavets.task_management_system.employee.service.Impl;

import com.moldavets.task_management_system.auth.model.Role;
import com.moldavets.task_management_system.auth.service.RoleService;
import com.moldavets.task_management_system.employee.dto.RequestEmployeeDto;
import com.moldavets.task_management_system.employee.model.Employee;
import com.moldavets.task_management_system.employee.repository.EmployeeRepository;
import com.moldavets.task_management_system.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @InjectMocks
    EmployeeServiceImpl employeeServiceImpl;

    @Mock
    EmployeeRepository employeeRepository;

    @Mock
    RoleService roleService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Employees can be returned")
    void getAllEmployees_shouldReturnEmployees_whenDatabaseIsNotEmpty() {
        List<Employee> employees = List.of(
                new Employee("John", "123", "john@mail.com", null, null),
                new Employee("Jack", "456", "jack@mail.com", null, null)
        );

        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> actual = employeeServiceImpl.getAll();

        assertEquals(employees, actual);
        assertEquals(employees.size(), actual.size());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Method should return empty array when database is empty")
    void getAllEmployees_shouldReturnEmptyArray_whenDatabaseIsEmpty() {
        List<Employee> employees = new ArrayList<>();
        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> expected = new ArrayList<>();

        assertEquals(expected, employeeServiceImpl.getAll());
        assertEquals(employees.isEmpty(), expected.isEmpty());

        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Employee can be returned by id")
    void getEmployeeById_shouldReturnEmployeeById_whenInputContainsCorrectId() {
        when(employeeRepository.findById(anyLong()))
                .thenReturn(Optional.of(new Employee("Jack", "123", "jack@mail.com", null, null)));

        Employee expected = new Employee("Jack", "123", "jack@mail.com", null, null);
        Employee actual = employeeServiceImpl.getById(1L);


        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getEmail(), actual.getEmail());

        verify(employeeRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Method should throw exception when employee do not exist")
    void getEmployeeById_shouldThrowException_whenEmployeeDoNotExist() {
        Long employeeId = 1L;
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        String expected = String.format("Employee with id %s not found", employeeId);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> employeeServiceImpl.getById(employeeId));

        assertEquals(expected, exception.getMessage());

        verify(employeeRepository, times(1)).findById(employeeId);
    }

    @Test
    @DisplayName("Method should throw exception when employee id is null")
    void getEmployeeById_shouldThrowException_whenEmployeeIdIsNull() {
        Long employeeId = null;
        String expected = "Id can not be null";

        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> employeeServiceImpl.getById(employeeId));

        assertEquals(expected, exception.getMessage());

        verify(employeeRepository, times(0)).findById(employeeId);
    }

    @Test
    @DisplayName("Employee can by returned by username")
    void getByUsername_shouldReturnEmployeeByUsername_whenInputContainsCorrectUsername() {
        Employee employee = new Employee("John", "123", "john@mail.com", null, null);

        when(employeeRepository.findEmployeeByUsername(anyString()))
                .thenReturn(Optional.of(employee));

        Employee expected = new Employee("John", "123", "john@mail.com", null, null);
        Employee actual = employeeServiceImpl.getByUsername(employee.getUsername());

        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getEmail(), actual.getEmail());

        verify(employeeRepository, times(1)).findEmployeeByUsername(anyString());
    }

    @Test
    @DisplayName("Method should throw exception when employee can not found by username")
    void getByUsername_shouldThrowException_whenEmployeeCanNotFoundByUsername() {
        String username = "test";
        when(employeeRepository.findEmployeeByUsername(anyString())).thenThrow(new ResourceNotFoundException(String.format("Employee with name %s not found", username)));

        String expected = String.format("Employee with name %s not found", username);
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> employeeServiceImpl.getByUsername(username));

        assertEquals(expected, exception.getMessage());

        verify(employeeRepository, times(1)).findEmployeeByUsername(username);
    }

    @Test
    @DisplayName("Method should throw exception when username is null")
    void getByUsername_shouldThrowException_whenUsernameIsNull() {
        String username = null;
        String expected = "Username can not be null";

        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> employeeServiceImpl.getByUsername(username));

        assertEquals(expected, exception.getMessage());

        verify(employeeRepository, times(0)).findEmployeeByUsername(username);
    }

    @Test
    @DisplayName("Should return true when employee exist")
    void isExistByUsername_shouldReturnTrue_whenInputContainsCorrectUsername() {
        when(employeeRepository.existsEmployeeByUsername(anyString())).thenReturn(true);

        assertTrue(employeeServiceImpl.isExistByUsername(anyString()));

        verify(employeeRepository, times(1)).existsEmployeeByUsername(anyString());
    }

    @Test
    @DisplayName("Should return false when employee does not exist")
    void isExistByUsername_shouldReturnFalse_whenEmployeeDoesNotExist() {
        when(employeeRepository.existsEmployeeByUsername(anyString())).thenReturn(false);

        assertFalse(employeeServiceImpl.isExistByUsername(anyString()));

        verify(employeeRepository, times(1)).existsEmployeeByUsername(anyString());
    }

    @Test
    @DisplayName("Employee can be stored")
    void save_shouldSaveEmployee_whenInputContainsCorrectEmployee() {
        Employee employee = new Employee("John", "123", "john@mail.com", null, null);

        when(passwordEncoder.encode(anyString())).thenReturn("321");
        when(roleService.getByName(anyString())).thenReturn(new Role("ROLE_EMPLOYEE"));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee expected = new Employee("John", "321", "john@mail.com", List.of(new Role("ROLE_EMPLOYEE")), null);

        Employee actual = employeeServiceImpl.save(employee);

        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getRoles(), actual.getRoles());

        verify(employeeRepository, times(1)).save(any());
        verify(roleService, times(1)).getByName(anyString());
        verify(passwordEncoder, times(1)).encode(anyString());
    }

    @Test
    @DisplayName("Employee can be updated")
    void update_shouldUpdateEmployee_whenInputContainsCorrectEmployee() {
        Long employeeId = 1L;
        RequestEmployeeDto requestEmployeeDto = new RequestEmployeeDto("jack","jack@mail.com", "123");

        Employee employee = new Employee("John", "456", "john@mail.com", null, null);

        Employee updatedEmployee = new Employee("John", "456", "john@mail.com", null, null);
        updatedEmployee.setUpdated(new Date());

        when(employeeRepository.findById(anyLong()))
                .thenReturn(Optional.of(employee));
        when(passwordEncoder.encode(anyString())).thenReturn("321");
        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);

        Employee actual = employeeServiceImpl.update(employeeId, requestEmployeeDto);

        assertEquals(updatedEmployee.getUsername(), actual.getUsername());
        assertEquals(updatedEmployee.getPassword(), actual.getPassword());
        assertEquals(updatedEmployee.getEmail(), actual.getEmail());
        assertNotNull(actual.getUpdated());

        verify(employeeRepository, times(1)).findById(anyLong());
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    @DisplayName("Method should throw exception when employee can not be find")
    void update_shouldThrowException_whenInputContainsNotExistingEmployeeId() {
        Long employeeId = 1L;
        RequestEmployeeDto requestEmployeeDto = new RequestEmployeeDto("jack","jack@mail.com", "123");

        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

        String expected = String.format("Employee with id %s not found", employeeId);
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> employeeServiceImpl.update(
                        employeeId,
                        requestEmployeeDto
                )
        );

        assertEquals(expected, exception.getMessage());

        verify(employeeRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Method should throw exception when employee id is null")
    void update_shouldThrowException_whenInputContainsNullEmployeeId() {
        RequestEmployeeDto requestEmployeeDto = new RequestEmployeeDto("jack","jack@mail.com", "123");
        assertThrows(NullPointerException.class,
                () -> employeeServiceImpl.update(
                        null,
                        requestEmployeeDto
                )
        );
    }

    @Test
    @DisplayName("Employee can be deleted")
    void deleteById_shouldDeleteEmployee_whenInputContainsCorrectEmployeeId() {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(new Employee("John", "456", "john@mail.com", null, null)));
        doNothing().when(employeeRepository).deleteById(anyLong());

        employeeServiceImpl.deleteById(1L);

        verify(employeeRepository, times(1)).findById(anyLong());
        verify(employeeRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Method should throw exception when employee does not exist")
    void deleteById_shouldThrowException_whenEmployeeDoesNotExist() {
        Long employeeId = 1L;
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

        String expected = String.format("Employee with id %s not found", employeeId);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> employeeServiceImpl.deleteById(employeeId));

        assertEquals(expected, exception.getMessage());

        verify(employeeRepository, times(1)).findById(anyLong());
        verify(employeeRepository, times(0)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Method should throw exception when employee is null")
    void deleteById_shouldThrowException_whenEmployeeIsNull() {
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> employeeServiceImpl.deleteById(null));

        assertEquals("Id can not be null", exception.getMessage());
        verify(employeeRepository, times(0)).findById(anyLong());
        verify(employeeRepository, times(0)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Should load user by username and return spring seccurity user")
    void loadUserByUsername_shouldReturnSpringSecurityUser_whenInputContainsCorrectUsername() {
        Employee employee = new Employee("John", "456", "john@mail.com", List.of(new Role("ROLE_EMPLOYEE"), new Role("ROLE_TEST")), null);

        when(employeeRepository.findEmployeeByUsername(anyString()))
                .thenReturn(Optional.of(employee));

        UserDetails actual = employeeServiceImpl.loadUserByUsername("test");

        assertEquals(employee.getUsername(), actual.getUsername());
        assertEquals(employee.getPassword(), actual.getPassword());
        assertEquals(employee.getRoles().size(), actual.getAuthorities().size());
        assertEquals(User.class, actual.getClass());

        verify(employeeRepository, times(1)).findEmployeeByUsername(anyString());
    }

}