package com.moldavets.task_management_system.employee.model;

import com.moldavets.task_management_system.utils.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    @Column(name = "name")
    String name;

    @ManyToMany(mappedBy = "roles")
    private List<Employee> employees;

}
