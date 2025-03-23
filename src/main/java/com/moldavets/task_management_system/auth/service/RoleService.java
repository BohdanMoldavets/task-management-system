package com.moldavets.task_management_system.auth.service;

import com.moldavets.task_management_system.auth.model.Role;

public interface RoleService {
    Role getByName(String name);
}
