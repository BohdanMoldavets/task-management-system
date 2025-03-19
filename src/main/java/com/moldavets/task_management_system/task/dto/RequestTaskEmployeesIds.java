package com.moldavets.task_management_system.task.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class RequestTaskEmployeesIds {

    @NotNull
    @JsonProperty("employeeIds")
    List<Long> employeeIds;

}
