package com.moldavets.task_management_system.auth.service.Impl;

import com.moldavets.task_management_system.auth.model.Role;
import com.moldavets.task_management_system.auth.repository.RoleRepository;
import com.moldavets.task_management_system.auth.service.RoleService;
import com.moldavets.task_management_system.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getByName(String name) {
        return roleRepository.findByName(name).orElseThrow(
                () -> new ResourceNotFoundException("Role with name " + name + " not found"));
    }
}
