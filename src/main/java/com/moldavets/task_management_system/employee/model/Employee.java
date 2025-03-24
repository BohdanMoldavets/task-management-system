package com.moldavets.task_management_system.employee.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.moldavets.task_management_system.auth.model.Role;
import com.moldavets.task_management_system.task.model.Task;
import com.moldavets.task_management_system.utils.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "employees")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee extends BaseEntity {

    @Column(name = "username")
    private String username;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "employees_roles",
            joinColumns = @JoinColumn(name = "employee_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private List<Role> roles;

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "employees_tasks",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id")
    )
    private List<Task> tasks;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        Employee employee = (Employee) object;
        return Objects.equals(username, employee.username) && Objects.equals(password, employee.password) && Objects.equals(email, employee.email) && Objects.equals(roles, employee.roles) && Objects.equals(tasks, employee.tasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), username, password, email, roles, tasks);
    }
}
