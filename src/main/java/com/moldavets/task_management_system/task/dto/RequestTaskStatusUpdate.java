package com.moldavets.task_management_system.task.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.moldavets.task_management_system.utils.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class RequestTaskStatusUpdate {
    @NotNull
    @JsonProperty("status")
    private TaskStatus status;
}
