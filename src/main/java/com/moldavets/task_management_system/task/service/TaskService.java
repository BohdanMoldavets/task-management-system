package com.moldavets.task_management_system.task.service;

import com.moldavets.task_management_system.task.dto.RequestTaskDto;
import com.moldavets.task_management_system.task.dto.RequestTaskEmployeesIds;
import com.moldavets.task_management_system.task.model.Task;
import com.moldavets.task_management_system.utils.enums.TaskStatus;

import java.util.List;

public interface TaskService {

    List<Task> getAll();
    Task getById(Long id);
    Task save(Task task);
    Task update(Long id, RequestTaskDto task);
    Task updateStatusById(Long id, TaskStatus taskStatus);
    Task assignEmployeesToTask(Long id, RequestTaskEmployeesIds taskEmployeesIds);
    void delete(Long id);
}
