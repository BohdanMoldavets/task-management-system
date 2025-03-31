package com.moldavets.task_management_system.task.mapper;

import com.moldavets.task_management_system.employee.model.Employee;
import com.moldavets.task_management_system.task.dto.RequestTaskDto;
import com.moldavets.task_management_system.task.dto.ResponseTaskDto;
import com.moldavets.task_management_system.task.model.Task;
import com.moldavets.task_management_system.utils.enums.TaskStatus;
import com.moldavets.task_management_system.utils.enums.TaskType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskMapperTest {

    @Test
    @DisplayName("Should convert RequestTaskDto to Task")
    void mapRequestTaskDto_shouldConvertRequestTaskDtoToTask_whenInputContainsCorrectRequest() {
        RequestTaskDto requestTaskDto = new RequestTaskDto("test", "testing", TaskType.BUG, TaskStatus.TODO);

        Task expected = new Task("test", "testing", TaskType.BUG, TaskStatus.TODO, null);
        Task actual = TaskMapper.mapRequestTaskDto(requestTaskDto);

        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getType(), actual.getType());
        assertEquals(expected.getStatus(), actual.getStatus());
        assertEquals(expected.getEmployees(), actual.getEmployees());
    }

    @Test
    @DisplayName("Should convert Task to ResponseTaskDto")
    void mapToResponseTaskDto_shouldConvertTaskToResponseTaskDto_whenInputContainsCorrectTask() {
        Task task = new Task("test", "testing", TaskType.BUG, TaskStatus.TODO, List.of(new Employee()));
        task.setId(123L);

        ResponseTaskDto expected = new ResponseTaskDto(123L, "test", "testing", TaskType.BUG, TaskStatus.TODO, null, null, List.of(new Employee()));
        ResponseTaskDto actual = TaskMapper.mapToResponseTaskDto(task);

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getType(), actual.getType());
        assertEquals(expected.getStatus(), actual.getStatus());
        assertEquals(expected.getEmployees(), actual.getEmployees());
    }
}