package com.moldavets.task_management_system.exception.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class ExceptionDetailsModel {
    private LocalDateTime timestamp;
    private String message;
    private String details;
}
