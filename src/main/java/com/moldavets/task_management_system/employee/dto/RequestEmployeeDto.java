package com.moldavets.task_management_system.employee.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class RequestEmployeeDto {

    @JsonProperty("username")
    @Pattern(regexp="^[A-Za-z]*$",message = "Username should contains only letters")
    private String username;

    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Email should be valid")
    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;
}
