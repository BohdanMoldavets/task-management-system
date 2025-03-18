package com.moldavets.task_management_system.employee.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@Builder
public class ResponseEmployeeDto {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    private String email;

    @JsonProperty("created")
    private Date created;

    public ResponseEmployeeDto() {
    }

    public ResponseEmployeeDto(Long id, String username, String email, Date created) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.created = created;
    }
}
