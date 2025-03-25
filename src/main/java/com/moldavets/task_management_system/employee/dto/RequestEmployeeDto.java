package com.moldavets.task_management_system.employee.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class RequestEmployeeDto {

    @JsonProperty("username")
    @NotEmpty
    @NotNull
    @Pattern(regexp="^[A-Za-z]*$",message = "Username should contains only letters")
    private String username;

    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Email should be valid")
    @NotEmpty
    @NotNull
    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    @NotEmpty
    @NotNull
    private String password;
}
