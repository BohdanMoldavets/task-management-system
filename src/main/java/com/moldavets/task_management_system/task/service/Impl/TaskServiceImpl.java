package com.moldavets.task_management_system.task.service.Impl;

import com.moldavets.task_management_system.email.service.EmailSenderService;
import com.moldavets.task_management_system.employee.model.Employee;
import com.moldavets.task_management_system.employee.repository.EmployeeRepository;
import com.moldavets.task_management_system.exception.ResourceNotFoundException;
import com.moldavets.task_management_system.task.dto.RequestTaskDto;
import com.moldavets.task_management_system.task.model.Task;
import com.moldavets.task_management_system.task.repository.TaskRepository;
import com.moldavets.task_management_system.task.service.TaskService;
import com.moldavets.task_management_system.utils.enums.TaskStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final EmployeeRepository employeeRepository;
    private final EmailSenderService emailSenderService;

    @Override
    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    @Override
    public Task getById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Task with id %s not found", id)));
    }

    @Override
    @Transactional
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    @Override
    @Transactional
    public Task update(Long id, RequestTaskDto task) {
        Task storedTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Task with id %s not found", id)));

        storedTask.setUpdated(new Date());
        storedTask.setTitle(task.getTitle());
        storedTask.setDescription(task.getDescription());
        storedTask.setStatus(task.getStatus());
        storedTask.setType(task.getType());

        return taskRepository.save(storedTask);
    }

    @Override
    @Transactional
    public Task updateStatusById(Long id, TaskStatus taskStatus) {
        Task storedTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Task with id %s not found", id)));

        storedTask.setUpdated(new Date());
        storedTask.setStatus(taskStatus);

        storedTask.getEmployees().forEach(employee -> emailSenderService.send(
                employee.getEmail(),
                "Changed Task Status",
                String.format("Dear, %s! The status of the task - \"%s\" has just been changed to %s", employee.getUsername(), storedTask.getTitle(), taskStatus)
        ));

        return taskRepository.save(storedTask);
    }

    @Override
    @Transactional
    public Task assignEmployeeToTask(Long id, Long employeeId) {
        Task storedTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Task with id %s not found", id)));

        Employee storedEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Employee with id %s not found", employeeId)));

        List<Employee> taskEmployees = storedTask.getEmployees();
        taskEmployees.add(storedEmployee);

        storedTask.setEmployees(taskEmployees);

        if(!storedEmployee.getTasks().contains(storedTask)) {
            storedEmployee.getTasks().add(storedTask);
            storedEmployee.setUpdated(new Date());
        }

        Task updatedTask = taskRepository.save(storedTask);
        employeeRepository.save(storedEmployee);

        emailSenderService.send(
                storedEmployee.getEmail(),
                "New Task",
                String.format("Hi, %s! You just received new task - %s", storedEmployee.getUsername(), updatedTask.getTitle())
        );
        return updatedTask;
    }

    @Override
    @Transactional
    public Task unassignEmployeeToTask(Long id, Long employeeId) {
        String resourceNotFoundExceptionMessage = String.format("Employee with id %s already unassigned to task with id %s", id, employeeId);

        Task storedTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Task with id %s not found", id)));

        Employee storedEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Employee with id %s not found", employeeId)));

        if (!storedTask.getEmployees().contains(storedEmployee)) {
            throw new ResourceNotFoundException(resourceNotFoundExceptionMessage);
        }
        storedTask.getEmployees().remove(storedEmployee);
        storedTask.setUpdated(new Date());

        if(!storedEmployee.getTasks().contains(storedTask)) {
            throw new ResourceNotFoundException(resourceNotFoundExceptionMessage);
        }
        storedEmployee.getTasks().remove(storedTask);
        storedEmployee.setUpdated(new Date());

        Task updatedTask = taskRepository.save(storedTask);
        employeeRepository.save(storedEmployee);
        return updatedTask;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Task with id %s not found", id)));
        taskRepository.deleteById(id);
    }
}
