package com.mh.auth.service.intailization;


import com.mh.auth.service.constant.PermissionEnum;
import com.mh.auth.service.constant.RoleEnum;
import com.mh.auth.service.model.Permission;
import com.mh.auth.service.model.Role;
import com.mh.auth.service.repository.PermissionRepository;
import com.mh.auth.service.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@Order(1)
public class DataInitializationService implements CommandLineRunner {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("=".repeat(60));
        log.info("Starting Data Initialization for Auth Service...");
        log.info("=".repeat(60));

        initializePermissions();
        initializeRoles();

        log.info("=".repeat(60));
        log.info("Data Initialization Completed Successfully!");
        log.info("=".repeat(60));
    }

    private void initializePermissions() {
        log.info("Initializing Permissions...");

        int createdCount = 0;
        int existingCount = 0;

        for (PermissionEnum permEnum : PermissionEnum.values()) {
            if (!permissionRepository.existsByName(permEnum.getName())) {
                Permission permission = Permission.builder()
                        .name(permEnum.getName())
                        .description(permEnum.getDescription())
                        .resource(permEnum.getResource())
                        .action(permEnum.getAction())
                        .build();

                permissionRepository.save(permission);
                createdCount++;
                log.debug("Created permission: {}", permEnum.getName());
            } else {
                existingCount++;
            }
        }

        log.info("Permissions: {} created, {} already existed, {} total",
                createdCount, existingCount, PermissionEnum.values().length);
    }

    private void initializeRoles() {
        log.info("Initializing Roles...");

        int createdCount = 0;
        int updatedCount = 0;
        int existingCount = 0;

        for (RoleEnum roleEnum : RoleEnum.values()) {
            if (!roleRepository.existsByName(roleEnum.getName())) {
                Set<Permission> permissions = getPermissionsForRole(roleEnum);

                Role role = Role.builder()
                        .name(roleEnum.getName())
                        .description(roleEnum.getDescription())
                        .permissions(permissions)
                        .build();

                roleRepository.save(role);
                createdCount++;
                log.debug("Created role: {} with {} permissions",
                        roleEnum.getName(), permissions.size());
            } else {
                roleRepository.findByNameWithPermissions(roleEnum.getName())
                        .ifPresent(existingRole -> {
                            Set<Permission> updatedPermissions = getPermissionsForRole(roleEnum);

                            if (!existingRole.getPermissions().equals(updatedPermissions)) {
                                existingRole.setPermissions(updatedPermissions);
                                existingRole.setDescription(roleEnum.getDescription());
                                roleRepository.save(existingRole);
                                log.debug("Updated role: {} with {} permissions",
                                        roleEnum.getName(), updatedPermissions.size());
                            }
                        });
                updatedCount++;
            }
        }

        log.info("Roles: {} created, {} updated, {} total",
                createdCount, updatedCount, RoleEnum.values().length);
    }

    private Set<Permission> getPermissionsForRole(RoleEnum roleEnum) {
        Set<Permission> permissions = new HashSet<>();

        for (PermissionEnum permEnum : roleEnum.getPermissions()) {
            permissionRepository.findByName(permEnum.getName())
                    .ifPresent(permissions::add);
        }

        return permissions;
    }
}