package com.moldavets.task_management_system.task.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moldavets.task_management_system.TaskManagementSystemApplication;
import com.moldavets.task_management_system.auth.utils.JwtTokenUtils;
import com.moldavets.task_management_system.employee.dto.ResponseEmployeeDto;
import com.moldavets.task_management_system.employee.model.Employee;
import com.moldavets.task_management_system.employee.service.EmployeeService;
import com.moldavets.task_management_system.exception.ResourceNotFoundException;
import com.moldavets.task_management_system.task.dto.RequestTaskDto;
import com.moldavets.task_management_system.task.dto.RequestTaskStatusUpdate;
import com.moldavets.task_management_system.task.model.Task;
import com.moldavets.task_management_system.task.service.TaskService;
import com.moldavets.task_management_system.utils.enums.TaskStatus;
import com.moldavets.task_management_system.utils.enums.TaskType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TaskManagementSystemApplication.class)
@WebMvcTest(value = TaskController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
class TaskControllerTest {

    @MockitoBean
    TaskService taskService;

    @MockitoBean
    EmployeeService employeeService;

    @MockitoBean
    JwtTokenUtils jwtTokenUtils;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("Tasks can be returned")
    void getAllTasks_shouldReturnAllTasks_whenInputContainsValidRequest() throws Exception {
        Task aTask = new Task("a", "description", TaskType.BUG, TaskStatus.IN_PROGRESS, null);
        Task bTask = new Task("b", "description", TaskType.IMPROVEMENT, TaskStatus.TODO, null);

        String tasksJson = objectMapper.writeValueAsString(List.of(aTask, bTask));

        when(taskService.getAll()).thenReturn(List.of(aTask, bTask));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/tasks"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(tasksJson));

        verify(taskService, times(1)).getAll();
    }

    @Test
    @DisplayName("Task can be return by id")
    void getTaskById_shouldReturnTask_whenInputContainsValidTaskId() throws Exception {
        Long taskId = 1L;
        Task aTask = new Task("a", "description", TaskType.BUG, TaskStatus.IN_PROGRESS, null);
        aTask.setId(taskId);

        String taskJson = objectMapper.writeValueAsString(aTask);

        when(taskService.getById(anyLong())).thenReturn(aTask);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/tasks/" + taskId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(taskJson));

        verify(taskService, times(1)).getById(anyLong());
    }

    @Test
    @DisplayName("Method should throw exception when task does not exist")
    void getTaskById_shouldThrowException_whenTaskDoesNotExist() throws Exception {
        Long taskId = 1L;
        when(taskService.getById(anyLong()))
                .thenThrow(new ResourceNotFoundException(String.format("Task with id %s not found", taskId)));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/tasks/" + taskId))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(String.format("Task with id %s not found", taskId)));
    }

    @Test
    @DisplayName("Task can be created")
    void createTask_shouldCreateTask_whenInputContainsValidRequestTaskDto() throws Exception {
        RequestTaskDto requestTaskDto = new RequestTaskDto("a", "description", TaskType.BUG, TaskStatus.IN_PROGRESS);
        Task task = new Task("a", "description", TaskType.BUG, TaskStatus.IN_PROGRESS, null);
        task.setId(1L);

        when(taskService.save(any(Task.class))).thenReturn(task);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestTaskDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(task.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(task.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(task.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value(task.getType().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(task.getStatus().toString()));

        verify(taskService, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("Task can be updated")
    void updateTaskById_shouldUpdateTask_whenInputContainsValidRequestTaskDto() throws Exception {
        Long taskId = 1L;
        RequestTaskDto requestTaskDto = new RequestTaskDto("b", "desc", TaskType.IMPROVEMENT, TaskStatus.DONE);
        Task task = new Task("b", "desc", TaskType.IMPROVEMENT, TaskStatus.DONE, null);
        task.setId(taskId);

         when(taskService.update(anyLong(), any(RequestTaskDto.class))).thenReturn(task);

         mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/tasks/" + taskId)
                     .contentType(MediaType.APPLICATION_JSON)
                     .content(objectMapper.writeValueAsString(requestTaskDto)))
                 .andExpect(MockMvcResultMatchers.status().isOk())
                 .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(task.getId()))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(task.getTitle()))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(task.getDescription()))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.type").value(task.getType().toString()))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(task.getStatus().toString()));

         verify(taskService, times(1)).update(anyLong(), any(RequestTaskDto.class));
    }

    @Test
    @DisplayName("Method should throw exception when task does not exist")
    void updateTaskById_shouldThrowException_whenTaskDoesNotExist() throws Exception {
        Long taskId = 1L;
        RequestTaskDto requestTaskDto = new RequestTaskDto("b", "desc", TaskType.IMPROVEMENT, TaskStatus.DONE);

        when(taskService.update(anyLong(), any(RequestTaskDto.class)))
                .thenThrow(new ResourceNotFoundException(String.format("Task with id %s not found", taskId)));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/tasks/" + taskId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestTaskDto)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(String.format("Task with id %s not found", taskId)));

        verify(taskService, times(1)).update(anyLong(), any(RequestTaskDto.class));
    }

    @Test
    @DisplayName("Task status can be patched when input contains valid request")
    void patchTaskStatusById_shouldPatchTaskStatus_whenInputContainsValidRequestTaskStatusUpdate() throws Exception {
        Long taskId = 1L;
        RequestTaskStatusUpdate requestStatus = new RequestTaskStatusUpdate(TaskStatus.IN_PROGRESS);

        Task task = new Task("A", "test", TaskType.BUG, TaskStatus.IN_PROGRESS, null);
        task.setId(taskId);

        when(taskService.updateStatusById(anyLong(), any(TaskStatus.class))).thenReturn(task);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/tasks/" + taskId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestStatus)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(taskId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(task.getStatus().toString()));

        verify(taskService, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
    }

    @Test
    @DisplayName("Method should throw exception when task does not exist")
    void patchTaskStatusById_shouldThrowException_whenTaskDoesNotExist() throws Exception {
        Long taskId = 1L;
        RequestTaskStatusUpdate requestStatus = new RequestTaskStatusUpdate(TaskStatus.IN_PROGRESS);

        when(taskService.updateStatusById(anyLong(), any(TaskStatus.class)))
                .thenThrow(new ResourceNotFoundException(String.format("Task with id %s not found", taskId)));

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/tasks/" + taskId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestStatus)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(String.format("Task with id %s not found", taskId)));

        verify(taskService, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
    }

    @Test
    @DisplayName("Task can be deleted by id")
    void deleteTaskById_shouldDeleteTask_whenInputContainsValidRequest() throws Exception {
        doNothing().when(taskService).delete(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/tasks/" + 1L))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(taskService, times(1)).delete(anyLong());
    }

    @Test
    @DisplayName("Method should throw exception when task does not exist")
    void deleteTaskById_shouldThrowException_whenTaskDoesNotExist() throws Exception {
        doThrow(ResourceNotFoundException.class).when(taskService).delete(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/tasks/" + 1L))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(taskService, times(1)).delete(anyLong());
    }

    //mappings with employees
    @Test
    @DisplayName("Assigned employees can be returned for task")
    void getAllEmployeesByTaskId_shouldReturnAssignedEmployeesToTask_whenInputContainsValidRequest() throws Exception {
        Long taskId = 1L;

        Employee john = new Employee("john", "123", "john@mail.com", null, null);
        john.setId(1L);

        Employee jack = new Employee("jack", "312", "jack@mail.com", null, null);
        jack.setId(2L);

        Task task = new Task("test","disc", TaskType.BUG, TaskStatus.IN_PROGRESS, List.of(john, jack));
        task.setId(taskId);

        when(taskService.getById(taskId)).thenReturn(task);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/tasks/" + taskId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(taskId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employees[0].id").value(john.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employees[0].username").value(john.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employees[0].email").value(john.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employees[1].id").value(jack.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employees[1].username").value(jack.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employees[1].email").value(jack.getEmail()));

        verify(taskService, times(1)).getById(anyLong());
    }

    @Test
    @DisplayName("Employee can be assigned to task")
    void assignEmployeeToTaskById_shouldAssignEmployeeToTask_whenInputContainsValidRequest() throws Exception {
        Long taskId = 1L;

        Employee employee = new Employee("john", "123", "john@mail.com", null, null);
        employee.setId(1L);

        Task task = new Task("test","disc", TaskType.BUG, TaskStatus.IN_PROGRESS, List.of(employee));
        task.setId(taskId);

        employee.setTasks(List.of(task));

        when(taskService.assignEmployeeToTask(anyLong(), anyLong())).thenReturn(task);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/tasks/1/employees/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.employees[0].id").value(employee.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employees[0].username").value(employee.getUsername()));

        verify(taskService, times(1)).assignEmployeeToTask(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Method should throw exception when task does not exist")
    void assignEmployeeToTaskById_shouldThrowException_whenTaskDoesNotExist() throws Exception {
        Long taskId = 1L;
        when(taskService.assignEmployeeToTask(anyLong(), anyLong()))
                .thenThrow(new ResourceNotFoundException(String.format("Task with id %s not found", taskId)));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/tasks/1/employees/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(String.format("Task with id %s not found", 1)));

        verify(taskService, times(1)).assignEmployeeToTask(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Method should throw exception when employee does not exist")
    void assignEmployeeToTaskById_shouldThrowException_whenEmployeeDoesNotExist() throws Exception {
        Long employeeId = 1L;

        when(taskService.assignEmployeeToTask(anyLong(), anyLong()))
                .thenThrow(new ResourceNotFoundException(String.format("Employee with id %s not found", employeeId)));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/tasks/1/employees/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(String.format("Employee with id %s not found", 1)));

        verify(taskService, times(1)).assignEmployeeToTask(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Assigned employee can be unassigned")
    void unassignEmployeeToTaskById_shouldUnassignEmployeeToTask_whenInputContainsValidRequest() throws Exception {
        Task task = new Task("test","disc", TaskType.BUG, TaskStatus.IN_PROGRESS, null);
        task.setId(1L);

        when(taskService.unassignEmployeeToTask(anyLong(), anyLong())).thenReturn(task);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/tasks/1/employees/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(taskService, times(1)).unassignEmployeeToTask(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Method should throw exception when task does not exist")
    void unassignEmployeeToTaskById_shouldThrowException_whenTaskDoesNotExist() throws Exception {
        Long taskId = 1L;
        when(taskService.unassignEmployeeToTask(anyLong(), anyLong()))
                .thenThrow(new ResourceNotFoundException(String.format("Task with id %s not found", taskId)));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/tasks/1/employees/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(String.format("Task with id %s not found", 1)));

        verify(taskService, times(1)).unassignEmployeeToTask(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Method should throw exception when employee does not exist")
    void unassignEmployeeToTaskById_shouldThrowException_whenEmployeeDoesNotExist() throws Exception {
        Long employeeId = 1L;

        when(taskService.unassignEmployeeToTask(anyLong(), anyLong()))
                .thenThrow(new ResourceNotFoundException(String.format("Employee with id %s not found", employeeId)));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/tasks/1/employees/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(String.format("Employee with id %s not found", 1)));

        verify(taskService, times(1)).unassignEmployeeToTask(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Method should throw exception when employee already unassigned to task")
    void unassignEmployeeToTaskById_shouldThrowException_whenEmployeeAlreadyUnassignedToTask() throws Exception {
        Long taskId = 1L;
        Long employeeId = 1L;

        when(taskService.unassignEmployeeToTask(anyLong(), anyLong()))
                .thenThrow(new ResourceNotFoundException(String.format("Employee with id %s already unassigned to task with id %s", taskId, employeeId)));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/tasks/1/employees/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Employee with id 1 already unassigned to task with id 1"));

        verify(taskService, times(1)).unassignEmployeeToTask(anyLong(), anyLong());
    }
}