package com.moldavets.task_management_system.task.mapper;

import com.moldavets.task_management_system.task.dto.RequestTaskDto;
import com.moldavets.task_management_system.task.dto.ResponseTaskDto;
import com.moldavets.task_management_system.task.model.Task;

public class TaskMapper {

    private TaskMapper() {
    }

    public static Task mapRequestTaskDto(RequestTaskDto requestTaskDto) {
        return Task.builder()
                .title(requestTaskDto.getTitle())
                .description(requestTaskDto.getDescription())
                .type(requestTaskDto.getType())
                .status(requestTaskDto.getStatus())
                .build();
    }

    public static ResponseTaskDto mapToResponseTaskDto(Task task) {
        return ResponseTaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .type(task.getType())
                .status(task.getStatus())
                .created(task.getCreated())
                .updated(task.getUpdated())
                .employees(task.getEmployees())
                .build();
    }

}
