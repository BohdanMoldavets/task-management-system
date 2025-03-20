package com.moldavets.task_management_system.task.service;

import com.moldavets.task_management_system.task.dto.RequestTaskDto;
import com.moldavets.task_management_system.task.model.Task;

import java.util.List;

public interface TaskService {
    List<Task> getAll();
    Task getById(Long id);
    Task save(Task task);
    Task update(Long taskId, RequestTaskDto task);
    void delete(Long id);
}
