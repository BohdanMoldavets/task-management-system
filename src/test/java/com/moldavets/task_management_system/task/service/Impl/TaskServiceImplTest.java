package com.moldavets.task_management_system.task.service.Impl;

import com.moldavets.task_management_system.email.service.EmailSenderService;
import com.moldavets.task_management_system.employee.model.Employee;
import com.moldavets.task_management_system.employee.repository.EmployeeRepository;
import com.moldavets.task_management_system.exception.ConflictException;
import com.moldavets.task_management_system.exception.ResourceNotFoundException;
import com.moldavets.task_management_system.task.dto.RequestTaskDto;
import com.moldavets.task_management_system.task.model.Task;
import com.moldavets.task_management_system.task.repository.TaskRepository;
import com.moldavets.task_management_system.utils.enums.TaskStatus;
import com.moldavets.task_management_system.utils.enums.TaskType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @InjectMocks
    TaskServiceImpl taskServiceImpl;

    @Mock
    TaskRepository taskRepository;

    @Mock
    EmployeeRepository employeeRepository;

    @Mock
    EmailSenderService emailSenderService;

    @Test
    @DisplayName("Tasks can be returned")
    void getAll_shouldReturnAllTasks_whenDatabaseIsNotEmpty() {
        List<Task> tasks = List.of(
                new Task("task1","desc1", TaskType.BUG, TaskStatus.TODO, null),
                new Task("task2","desc2", TaskType.IMPROVEMENT, TaskStatus.IN_PROGRESS, null)
        );
        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> actual = taskServiceImpl.getAll();

        assertEquals(tasks, actual);
        assertEquals(tasks.size(), actual.size());

        verify(taskRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Tasks can be returned when database is empty")
    void getAll_shouldReturnEmptyArray_whenDatabaseIsEmpty() {
        when(taskRepository.findAll()).thenReturn(new ArrayList<>());

        List<Task> actual = taskServiceImpl.getAll();

        assertTrue(actual.isEmpty());

        verify(taskRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Task can be returned by id")
    void getById_shouldReturnTask_whenInputContainsCorrectId() {
        Long taskId = 1L;

        Task task = new Task("task","desc", TaskType.BUG, TaskStatus.TODO, null);
        task.setId(taskId);

        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));

        Task actual = taskServiceImpl.getById(taskId);

        assertEquals(taskId, actual.getId());
        assertEquals(task.getTitle(), actual.getTitle());
        assertEquals(task.getDescription(), actual.getDescription());

        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    @DisplayName("Method should throw exception when task does not exist")
    void getById_shouldThrowException_whenTaskDoesNotExist() {
        Long taskId = 1L;
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        String expected = "Task with id " + taskId + " not found";

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> taskServiceImpl.getById(taskId));

        assertEquals(expected, exception.getMessage());

        verify(taskRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Method should throw exception when id is null")
    void getById_shouldThrowException_whenIdIsNull() {
        Long taskId = null;

        String expected = "Id can not be null";

        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> taskServiceImpl.getById(taskId));

        assertEquals(expected, exception.getMessage());
    }

    @Test
    @DisplayName("Task can be saved")
    void save_shouldSaveTask_whenInputContainsCorrectTask() {
        Long taskId = 1L;

        Task task = new Task("task", "desc", TaskType.FIX, TaskStatus.IN_PROGRESS, null);
        task.setId(1L);

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task actual = taskServiceImpl.save(new Task("task", "desc", TaskType.FIX, TaskStatus.IN_PROGRESS, null));

        assertEquals(taskId, actual.getId());
        assertEquals(task.getTitle(), actual.getTitle());
        assertEquals(task.getDescription(), actual.getDescription());

        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("Task can be updated")
    void update_shouldUpdateTask_whenInputContainsCorrectTask() {
        Long taskId = 1L;
        RequestTaskDto requestTaskDto = new RequestTaskDto("task", "desc", TaskType.FIX, TaskStatus.IN_PROGRESS);

        Task task = new Task("test", "testing", TaskType.BUG, TaskStatus.TODO, null);
        task.setId(taskId);

        Task updatedTask = new Task("task", "desc", TaskType.FIX, TaskStatus.IN_PROGRESS, null);
        updatedTask.setId(taskId);
        updatedTask.setUpdated(new Date());

        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        Task actual = taskServiceImpl.update(taskId, requestTaskDto);

        assertEquals(taskId, actual.getId());
        assertEquals(requestTaskDto.getTitle(), actual.getTitle());
        assertEquals(requestTaskDto.getDescription(), actual.getDescription());
        assertEquals(requestTaskDto.getType(), actual.getType());
        assertEquals(requestTaskDto.getStatus(), actual.getStatus());
        assertNotNull(actual.getUpdated());

        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    @DisplayName("Method should throw exception when task does not exist")
    void update_shouldThrowException_whenTaskDoesNotExist() {
        Long taskId = 1L;
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        String expected = "Task with id " + taskId + " not found";
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> taskServiceImpl.update(taskId, null));

        assertEquals(expected, exception.getMessage());

        verify(taskRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Method should throw exception when task id is null")
    void update_shouldThrowException_whenTaskIdIsNull() {
        String expected = "Id can not be null";

        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> taskServiceImpl.update(null, null));

        assertEquals(expected, exception.getMessage());

        verify(taskRepository, times(0)).findById(anyLong());
    }

    @Test
    @DisplayName("Task status can be updated")
    void updateStatusById_shouldChangeTaskStatus_whenInputContainsCorrectTaskStatus() {
        Long taskId = 1L;
        TaskStatus status = TaskStatus.IN_PROGRESS;
        Employee employee = new Employee("Jack", "123", "Jack@mail.com", null, null);

        Task task = new Task("task", "desc", TaskType.FIX, TaskStatus.TODO,List.of(employee));
        task.setId(taskId);

        Task updatedTask = new Task("task", "desc", TaskType.FIX, TaskStatus.IN_PROGRESS, List.of(employee));
        updatedTask.setId(taskId);
        updatedTask.setUpdated(new Date());

        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));
        doNothing().when(emailSenderService).send(anyString(), anyString(), anyString());
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        Task actual = taskServiceImpl.updateStatusById(taskId, status);

        assertEquals(taskId, actual.getId());
        assertEquals(TaskStatus.IN_PROGRESS, actual.getStatus());
        assertEquals(task.getTitle(), actual.getTitle());
        assertEquals(task.getDescription(), actual.getDescription());
        assertNotNull(actual.getUpdated());

        verify(taskRepository, times(1)).findById(taskId);
        verify(emailSenderService, times(1)).send(anyString(), anyString(), anyString());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("Method should throw exception when task does not exist")
    void updateStatusById_shouldThrowException_whenTaskDoesNotExist() {
        Long taskId = 1L;
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        String expected = "Task with id " + taskId + " not found";
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> taskServiceImpl.updateStatusById(taskId, null));

        assertEquals(expected, exception.getMessage());

        verify(taskRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Method should throw exception when task id is null")
    void updateStatusById_shouldThrowException_whenTaskIdIsNull() {
        String expected = "Id can not be null";

        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> taskServiceImpl.updateStatusById(null, null));

        assertEquals(expected, exception.getMessage());

        verify(taskRepository, times(0)).findById(anyLong());
    }

    @Test
    @DisplayName("Employee can be assigned to task")
    void assignEmployeeToTask_shouldAssignEmployeeToTask_whenInputContainsCorrectData() {
        Long taskId = 1L;
        Long employeeId = 1L;

        Task task = new Task("task", "desc", TaskType.FIX, TaskStatus.IN_PROGRESS, new ArrayList<>());
        task.setId(taskId);

        Employee employee = new Employee("Jack", "123", "Jack@mail.com", null, new ArrayList<>());
        employee.setId(employeeId);

        Employee updatedEmployee = new Employee("Jack", "123", "Jack@mail.com", null, List.of(task));
        updatedEmployee.setId(employeeId);

        Task updatedTask = new Task("task", "desc", TaskType.FIX, TaskStatus.IN_PROGRESS, List.of(employee));
        updatedTask.setId(taskId);

        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);
        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);
        doNothing().when(emailSenderService).send(anyString(), anyString(), anyString());

        Task actual = taskServiceImpl.assignEmployeeToTask(taskId, employeeId);

        assertEquals(taskId, actual.getId());
        assertEquals(employee, actual.getEmployees().getFirst());

        verify(taskRepository, times(1)).findById(taskId);
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(taskRepository, times(1)).save(any(Task.class));
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    @DisplayName("Method should throw exception when employee already assigned")
    void assignEmployeeToTask_shouldThrowException_whenEmployeeAlreadyAssigned() {
        Long taskId = 1L;
        Long employeeId = 1L;

        Task task = new Task("task", "desc", TaskType.FIX, TaskStatus.IN_PROGRESS, null);
        task.setId(taskId);

        Employee employee = new Employee("Jack", "123", "Jack@mail.com", null, List.of(task));
        employee.setId(employeeId);

        task.setEmployees(List.of(employee));

        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));

        String expected = "Employee with id " + employeeId + " already assigned to task with id " + taskId;
        ConflictException exception = assertThrows(ConflictException.class,
                () -> taskServiceImpl.assignEmployeeToTask(taskId, employeeId));

        assertEquals(expected, exception.getMessage());

        verify(taskRepository, times(1)).findById(taskId);
        verify(employeeRepository, times(1)).findById(employeeId);
    }

    @Test
    @DisplayName("Method should throw exception when task does not exist")
    void assignEmployeeToTask_shouldThrowException_whenTaskDoesNotExist() {
        Long taskId = 1L;
        Long employeeId = 1L;
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        String expected = "Task with id " + taskId + " not found";
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> taskServiceImpl.assignEmployeeToTask(taskId, employeeId));

        assertEquals(expected, exception.getMessage());

        verify(taskRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Method should throw exception when employee does not exist")
    void assignEmployeeToTask_shouldThrowException_whenEmployeeDoesNotExist() {
        Long taskId = 1L;
        Long employeeId = 1L;
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(new Task()));

        String expected = "Employee with id " + employeeId + " not found";
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> taskServiceImpl.assignEmployeeToTask(taskId, employeeId));

        assertEquals(expected, exception.getMessage());

        verify(taskRepository, times(1)).findById(anyLong());
        verify(employeeRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Method should throw exception when task id is null")
    void assignEmployeeToTask_shouldThrowException_whenTaskIdIsNull() {
        Long taskId = null;
        Long employeeId = 1L;

        String expected = "Id can not be null";
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> taskServiceImpl.assignEmployeeToTask(taskId, employeeId));

        assertEquals(expected, exception.getMessage());
    }

    @Test
    @DisplayName("Method should throw exception when employee id is null")
    void assignEmployeeToTask_shouldThrowException_whenEmployeeIdIsNull() {
        Long taskId = 1L;
        Long employeeId = null;

        String expected = "Id can not be null";
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> taskServiceImpl.assignEmployeeToTask(taskId, employeeId));

        assertEquals(expected, exception.getMessage());
    }

    @Test
    @DisplayName("Employee can be unassigned from an task")
    void unassignEmployeeFromTask_shouldUnassignEmployeeFromTask() {
        Long taskId = 1L;
        Long employeeId = 1L;

        Employee employee = new Employee("Jack", "123", "Jack@mail.com", null, null);
        employee.setId(employeeId);

        Task task = new Task("task", "desc", TaskType.FIX, TaskStatus.IN_PROGRESS, new ArrayList<>(List.of(employee)));
        task.setId(taskId);
        System.out.println(task.getEmployees());

        employee.setTasks(new ArrayList<>(List.of(task)));

        Task updatedTask = new Task("task", "desc", TaskType.FIX, TaskStatus.IN_PROGRESS, new ArrayList<>());
        updatedTask.setId(taskId);
        updatedTask.setUpdated(new Date());

        Employee updatedEmployee = new Employee("Jack", "123", "Jack@mail.com", null, new ArrayList<>());
        updatedEmployee.setId(employeeId);
        updatedEmployee.setUpdated(new Date());
        System.out.println(task.getEmployees());

        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);
        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);

        Task actual = taskServiceImpl.unassignEmployeeFromTask(taskId, employeeId);

        assertEquals(taskId, actual.getId());
        assertTrue(actual.getEmployees().isEmpty());
        assertNotNull(actual.getUpdated());

        verify(taskRepository, times(1)).findById(taskId);
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(taskRepository, times(1)).save(any(Task.class));
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    @DisplayName("Method should throw exception when task does not exist")
    void unassignEmployeeFromTask_shouldThrowException_whenTaskDoesNotExist() {
        Long taskId = 1L;
        Long employeeId = 1L;

        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        String expected = "Task with id " + taskId + " not found";
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> taskServiceImpl.unassignEmployeeFromTask(taskId, employeeId));

        assertEquals(expected, exception.getMessage());

        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    @DisplayName("Method should throw exception when employee does not exist")
    void unassignEmployeeFromTask_shouldThrowException_whenEmployeeDoesNotExist() {
        Long taskId = 1L;
        Long employeeId = 1L;

        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(new Task()));
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

        String expected = "Employee with id " + employeeId + " not found";
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> taskServiceImpl.unassignEmployeeFromTask(taskId, employeeId));

        assertEquals(expected, exception.getMessage());

        verify(taskRepository, times(1)).findById(taskId);
        verify(employeeRepository, times(1)).findById(taskId);
    }

    @Test
    @DisplayName("Method should throw exception when task id is null")
    void unassignEmployeeFromTask_shouldThrowException_whenTaskIdIsNull() {
        Long taskId = null;
        Long employeeId = 1L;

        String expected = "Id can not be null";
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> taskServiceImpl.unassignEmployeeFromTask(taskId, employeeId));

        assertEquals(expected, exception.getMessage());

        verify(taskRepository, times(0)).findById(taskId);
    }

    @Test
    @DisplayName("Method should throw exception when employee id is null")
    void unassignEmployeeFromTask_shouldThrowException_whenEmployeeIdIsNull() {
        Long taskId = 1L;
        Long employeeId = null;

        String expected = "Id can not be null";
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> taskServiceImpl.unassignEmployeeFromTask(taskId, employeeId));

        assertEquals(expected, exception.getMessage());

        verify(employeeRepository, times(0)).findById(taskId);
    }

    @Test
    @DisplayName("Task can be deleted")
    void delete_shouldDeleteTask_whenInputContainsCorrectTaskId() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(new Task()));
        doNothing().when(taskRepository).deleteById(any());

        taskServiceImpl.delete(1L);

        verify(taskRepository, times(1)).findById(anyLong());
        verify(taskRepository, times(1)).deleteById(any());
    }

    @Test
    @DisplayName("Method should throw exception when task does not exist")
    void delete_shouldThrowException_whenTaskDoesNotExist() {
        Long taskId = 1L;
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        String expected = "Task with id " + taskId + " not found";
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> taskServiceImpl.delete(taskId));

        assertEquals(expected, exception.getMessage());

        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    @DisplayName("Method should throw exception when task id is null")
    void delete_shouldThrowException_whenTaskIdIsNull() {
        Long taskId = null;

        String expected = "Id can not be null";
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> taskServiceImpl.delete(taskId));

        assertEquals(expected, exception.getMessage());

        verify(taskRepository, times(0)).findById(anyLong());
    }
}