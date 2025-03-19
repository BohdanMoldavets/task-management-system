package com.moldavets.task_management_system.task.controller;

import com.moldavets.task_management_system.task.dto.RequestTaskDto;
import com.moldavets.task_management_system.task.dto.ResponseTaskDto;
import com.moldavets.task_management_system.task.mapper.TaskMapper;
import com.moldavets.task_management_system.task.model.Task;
import com.moldavets.task_management_system.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<ResponseTaskDto>> getAllTasks() {
        return new ResponseEntity<>(taskService.getAll().stream()
                .map(TaskMapper::mapToResponseTaskDto)
                .toList(),
                HttpStatus.OK);
    }

//    todo
//    @GetMapping("/{taskId}")
//    public ResponseEntity<ResponseTaskDto> getTaskById(@PathVariable("taskId")  Long taskId) {
//        return null;
//    }

    @PostMapping
    public ResponseEntity<ResponseTaskDto> createTask(@Valid @RequestBody RequestTaskDto requestTaskDto) {
        Task storedTask = taskService.save(TaskMapper.mapRequestTaskDto(requestTaskDto));
        return new ResponseEntity<>(
                TaskMapper.mapToResponseTaskDto(storedTask),
                HttpStatus.CREATED
        );
    }

}
