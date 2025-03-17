package com.moldavets.task_management_system.task.model;

import com.moldavets.task_management_system.employee.model.Employee;
import com.moldavets.task_management_system.utils.entity.BaseEntity;
import com.moldavets.task_management_system.utils.enums.TaskStatus;
import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name = "tasks")
public class Task extends BaseEntity {

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "taskStatus")
    private TaskStatus taskStatus;

    @ManyToMany(mappedBy = "tasks")
    private List<Employee> employees;
}
