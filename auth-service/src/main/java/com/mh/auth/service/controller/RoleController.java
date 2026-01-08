package com.mh.auth.service.controller;

import com.mh.auth.service.dto.respone.RoleResponse;
import com.mh.auth.service.mapper.RoleMapper;
import com.mh.auth.service.model.Role;
import com.mh.auth.service.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for role operations.
 * Provides read-only access to roles and their permissions.
 */
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Slf4j
public class RoleController {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    /**
     * Get all available roles with their permissions.
     * Requires ROLE_READ permission.
     *
     * @return list of all roles
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_READ')")
    public ResponseEntity<List<RoleResponse>> getAllRoles() {
        log.info("Get all roles endpoint called");
        List<Role> roles = roleRepository.findAll();
        List<RoleResponse> response = roleMapper.toRoleResponseList(roles);
        return ResponseEntity.ok(response);
    }

    /**
     * Get specific role by ID with its permissions.
     * Requires ROLE_READ permission.
     *
     * @param roleId role identifier
     * @return role details with permissions
     */
    @GetMapping("/{roleId}")
    @PreAuthorize("hasAuthority('ROLE_READ')")
    public ResponseEntity<RoleResponse> getRoleById(@PathVariable Long roleId) {
        log.info("Get role by ID endpoint called for role ID: {}", roleId);
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        RoleResponse response = roleMapper.toRoleResponse(role);
        return ResponseEntity.ok(response);
    }

    /**
     * Get role by name with its permissions.
     * Requires ROLE_READ permission.
     *
     * @param roleName role name
     * @return role details with permissions
     */
    @GetMapping("/name/{roleName}")
    @PreAuthorize("hasAuthority('ROLE_READ')")
    public ResponseEntity<RoleResponse> getRoleByName(@PathVariable String roleName) {
        log.info("Get role by name endpoint called for role: {}", roleName);
        Role role = roleRepository.findByNameWithPermissions(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        RoleResponse response = roleMapper.toRoleResponse(role);
        return ResponseEntity.ok(response);
    }
}
