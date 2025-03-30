package com.moldavets.task_management_system.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JwtRequest {

    @NotNull
    @NotEmpty
    @JsonProperty("username")
    private String username;

    @NotNull
    @NotEmpty
    @JsonProperty("password")
    private String password;
}
