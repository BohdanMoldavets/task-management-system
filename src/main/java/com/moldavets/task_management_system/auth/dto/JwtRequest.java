package com.moldavets.task_management_system.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JwtRequest {

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;
}
