package com.moldavets.task_management_system.task.service.Impl;

import com.moldavets.task_management_system.exception.ResourceNotFoundException;
import com.moldavets.task_management_system.task.dto.RequestTaskDto;
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
    public Task getById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Task with id %s not found", id)));
    }

    @Override
    @Transactional
    public Task save(Task task) {
        task.setCreated(new Date());
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
    public void delete(Long id) {
        taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Task with id %s not found", id)));
        taskRepository.deleteById(id);
    }
}
