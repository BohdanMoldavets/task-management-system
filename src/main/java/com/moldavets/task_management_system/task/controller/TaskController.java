package com.moldavets.task_management_system.task.controller;

import com.moldavets.task_management_system.employee.dto.ResponseEmployeeDto;
import com.moldavets.task_management_system.employee.mapper.EmployeeMapper;
import com.moldavets.task_management_system.employee.service.EmployeeService;
import com.moldavets.task_management_system.task.dto.RequestTaskDto;
import com.moldavets.task_management_system.task.dto.RequestTaskStatusUpdate;
import com.moldavets.task_management_system.task.dto.ResponseTaskDto;
import com.moldavets.task_management_system.task.mapper.TaskMapper;
import com.moldavets.task_management_system.task.model.Task;
import com.moldavets.task_management_system.task.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "TaskController")
@RestController
@RequestMapping("/api/v1/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final EmployeeService employeeService;

    @Operation(summary = "Get all the tasks")
    @GetMapping
    public ResponseEntity<List<ResponseTaskDto>> getAllTasks() {
        return new ResponseEntity<>(taskService.getAll().stream()
                .map(TaskMapper::mapToResponseTaskDto)
                .toList(),
                HttpStatus.OK);
    }

    @Operation(summary = "Get the task by id")
    @GetMapping("/{taskId}")
    public ResponseEntity<ResponseTaskDto> getTaskById(@PathVariable("taskId")  Long taskId) {
        return new ResponseEntity<>(
                TaskMapper.mapToResponseTaskDto(taskService.getById(taskId)),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Create the task")
    @PostMapping
    public ResponseEntity<ResponseTaskDto> createTask(@Valid @RequestBody RequestTaskDto requestTaskDto) {
        Task storedTask = taskService.save(TaskMapper.mapRequestTaskDto(requestTaskDto));
        return new ResponseEntity<>(
                TaskMapper.mapToResponseTaskDto(storedTask),
                HttpStatus.CREATED
        );
    }

    @Operation(summary = "Update the task by id")
    @PutMapping("/{taskId}")
    public ResponseEntity<ResponseTaskDto> updateTaskById(@PathVariable("taskId") Long taskId,
                                                          @Valid @RequestBody RequestTaskDto requestTaskDto) {
        return new ResponseEntity<>(
                TaskMapper.mapToResponseTaskDto(taskService.update(taskId, requestTaskDto)),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Update the task status by id")
    @PatchMapping("/{taskId}")
    public ResponseEntity<ResponseTaskDto> patchTaskStatusById(@PathVariable("taskId") Long taskId,
                                                               @RequestBody RequestTaskStatusUpdate requestTaskStatus) {
        return new ResponseEntity<>(
                TaskMapper.mapToResponseTaskDto(taskService.updateStatusById(taskId, requestTaskStatus.getStatus())),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Delete the task by id")
    @DeleteMapping("/{taskId}")
    public ResponseEntity<HttpStatusCode> deleteTaskById(@PathVariable("taskId") Long taskId) {
        taskService.delete(taskId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //mappings with employees
    @Operation(summary = "Get all employees assigned to task")
    @GetMapping("/{taskId}/employees")
    public ResponseEntity<List<ResponseEmployeeDto>> getAllEmployeesByTaskId(@PathVariable("taskId") Long taskId) {
        return new ResponseEntity<>(taskService.getById(taskId).getEmployees().stream()
                .map(EmployeeMapper::mapToResponseEmployeeDto)
                .toList(),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Assign the employee to task by id")
    @PostMapping("/{taskId}/employees/{employeeId}")
    public ResponseEntity<ResponseTaskDto> assignEmployeeToTaskById(@PathVariable("taskId") Long taskId,
                                                                    @PathVariable("employeeId") Long employeeId) {
        Task updatedTask = taskService.assignEmployeeToTask(taskId, employeeId);
        return new ResponseEntity<>(TaskMapper.mapToResponseTaskDto(updatedTask), HttpStatus.OK);
    }

    @Operation(summary = "Unassign the employee from task by id")
    @DeleteMapping("/{taskId}/employees/{employeeId}")
    public ResponseEntity<ResponseTaskDto> unassignEmployeeFromTaskById(@PathVariable("taskId") Long taskId,
                                                                        @PathVariable("employeeId") Long employeeId) {
        Task updatedTask = taskService.unassignEmployeeFromTask(taskId, employeeId);
        return new ResponseEntity<>(TaskMapper.mapToResponseTaskDto(updatedTask), HttpStatus.NO_CONTENT);
    }

}
