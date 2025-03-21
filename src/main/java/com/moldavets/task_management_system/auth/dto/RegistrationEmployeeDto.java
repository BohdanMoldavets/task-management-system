package com.moldavets.task_management_system.auth.dto;

import lombok.Data;

@Data
public class RegistrationEmployeeDto {
    private String username;
    private String password;
    private String confirmPassword;
    private String email;
}
