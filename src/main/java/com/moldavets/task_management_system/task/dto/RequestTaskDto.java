package com.moldavets.task_management_system.task.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.moldavets.task_management_system.utils.enums.TaskStatus;
import com.moldavets.task_management_system.utils.enums.TaskType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestTaskDto {

    @NotNull
    @Size(min = 1, max = 255, message = "Title length should be greater than 1 character or less than 255 characters")
    @JsonProperty("title")
    private String title;

    @NotNull
    @JsonProperty("description")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @JsonProperty("type")
    private TaskType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    @JsonProperty("status")
    private TaskStatus status;

}
