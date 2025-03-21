package com.moldavets.task_management_system.auth.service.Impl;

import com.moldavets.task_management_system.auth.model.Role;
import com.moldavets.task_management_system.auth.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    @Override
    public Role findByName(String roleName) {
        return null;
    }
}
