package com.moldavets.task_management_system.task.service.Impl;

import com.moldavets.task_management_system.task.model.Task;
import com.moldavets.task_management_system.task.repository.TaskRepository;
import com.moldavets.task_management_system.task.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    @Override
    @Transactional
    public Task save(Task task) {
        task.setCreated(new Date());
        return taskRepository.save(task);
    }
}
