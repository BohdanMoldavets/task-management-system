package com.moldavets.task_management_system.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.moldavets.task_management_system.employee.dto.RequestEmployeeDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RequestRegistrationEmployeeDto extends RequestEmployeeDto {
    @JsonProperty("confirm_password")
    @NotEmpty
    @NotNull
    private String confirmPassword;
}
