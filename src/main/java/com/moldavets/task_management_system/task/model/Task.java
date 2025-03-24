package com.moldavets.task_management_system.task.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.moldavets.task_management_system.employee.model.Employee;
import com.moldavets.task_management_system.utils.entity.BaseEntity;
import com.moldavets.task_management_system.utils.enums.TaskStatus;
import com.moldavets.task_management_system.utils.enums.TaskType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "tasks")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task extends BaseEntity {

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TaskType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TaskStatus status;

    @ManyToMany(mappedBy = "tasks")
    @JsonBackReference
    private List<Employee> employees;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        Task task = (Task) object;
        return Objects.equals(title, task.title) && Objects.equals(description, task.description) && type == task.type && status == task.status && Objects.equals(employees, task.employees);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, description, type, status, employees);
    }
}
