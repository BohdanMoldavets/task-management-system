package com.moldavets.task_management_system.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.moldavets.task_management_system.employee.model.Employee;
import com.moldavets.task_management_system.utils.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Table(name = "roles")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseEntity {

    @Column(name = "name")
    String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private List<Employee> employees;

    public Role(String name) {
        this.name = name;
    }
}
