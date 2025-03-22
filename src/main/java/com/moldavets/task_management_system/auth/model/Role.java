package com.moldavets.task_management_system.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.moldavets.task_management_system.employee.model.Employee;
import com.moldavets.task_management_system.utils.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "roles")
public class Role extends BaseEntity {

    @Column(name = "name")
    String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private List<Employee> employees;

}
