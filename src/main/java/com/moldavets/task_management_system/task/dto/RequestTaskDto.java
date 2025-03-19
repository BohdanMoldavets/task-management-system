package com.moldavets.task_management_system.task.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.moldavets.task_management_system.employee.model.Employee;
import com.moldavets.task_management_system.utils.enums.TaskStatus;
import com.moldavets.task_management_system.utils.enums.TaskType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestTaskDto {

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @Enumerated(EnumType.STRING)
    @JsonProperty("type")
    private TaskType type;

    @Enumerated(EnumType.STRING)
    @JsonProperty("status")
    private TaskStatus status;

}
