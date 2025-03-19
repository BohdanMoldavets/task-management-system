package com.moldavets.task_management_system.task.controller;

import com.moldavets.task_management_system.task.model.Task;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@AllArgsConstructor
public class TaskController {


    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        //todo
        return null;
    }

}
