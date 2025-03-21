package com.moldavets.task_management_system.employee.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.moldavets.task_management_system.auth.model.Role;
import com.moldavets.task_management_system.task.model.Task;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class ResponseEmployeeDto {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    private String email;

    @JsonProperty("created")
    private Date created;

    @JsonProperty("updated")
    private Date updated;

    @JsonProperty("roles")
    private List<Role> roles;

    @JsonProperty("tasks")
    private List<Task> tasks;

    public ResponseEmployeeDto() {
    }

    public ResponseEmployeeDto(Long id, String username, String email, Date created, Date updated, List<Role> roles, List<Task> tasks) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.created = created;
        this.updated = updated;
        this.roles = roles;
        this.tasks = tasks;
    }
}
