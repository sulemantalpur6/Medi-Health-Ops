package com.mh.auth.service.service.impl;

import com.mh.auth.service.dto.request.AssignRoleRequest;
import com.mh.auth.service.dto.respone.MessageResponse;
import com.mh.auth.service.dto.respone.UserRolePermissionResponse;
import com.mh.auth.service.exception.BadRequestException;
import com.mh.auth.service.exception.ResourceNotFoundException;
import com.mh.auth.service.mapper.UserRolePermissionMapper;
import com.mh.auth.service.model.Role;
import com.mh.auth.service.model.User;
import com.mh.auth.service.repository.RoleRepository;
import com.mh.auth.service.repository.UserRepository;
import com.mh.auth.service.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserManagementServiceImpl implements UserManagementService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRolePermissionMapper userRolePermissionMapper;

    /**
     * Get user's roles and permissions by user ID.
     *
     * @param userId user identifier
     * @return user roles and permissions
     * @throws ResourceNotFoundException if user not found
     */
    @Override
    @Transactional(readOnly = true)
    public UserRolePermissionResponse getUserRolesAndPermissions(Long userId) {
        User user = userRepository.findByIdWithRoles(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return userRolePermissionMapper.toUserRolePermissionResponse(user);
    }

    /**
     * Get all users with their roles and permissions.
     *
     * @return list of all users with roles
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserRolePermissionResponse> getAllUsersWithRoles() {
        return userRepository.findAll().stream()
                .map(userRolePermissionMapper::toUserRolePermissionResponse)
                .collect(Collectors.toList());
    }

    /**
     * Assign multiple roles to a user, replacing existing roles.
     *
     * @param request role assignment details
     * @return success message
     * @throws ResourceNotFoundException if user or role not found
     */
    @Override
    @Transactional
    public MessageResponse assignRolesToUser(AssignRoleRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Set<Role> roles = new HashSet<>();
        for (String roleName : request.getRoleNames()) {
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName));
            roles.add(role);
        }

        user.setRoles(roles);
        userRepository.save(user);

        log.info("Assigned roles {} to user: {}", request.getRoleNames(), user.getEmail());
        return new MessageResponse("Roles assigned successfully");
    }

    /**
     * Add a single role to user's existing roles.
     *
     * @param userId   user identifier
     * @param roleName role name to add
     * @return success message
     * @throws ResourceNotFoundException if user or role not found
     */
    @Override
    @Transactional
    public MessageResponse addRoleToUser(Long userId, String roleName) {
        User user = userRepository.findByIdWithRoles(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName));

        user.getRoles().add(role);
        userRepository.save(user);

        log.info("Added role {} to user: {}", roleName, user.getEmail());
        return new MessageResponse("Role added successfully");
    }

    /**
     * Remove a role from user.
     *
     * @param userId   user identifier
     * @param roleName role name to remove
     * @return success message
     * @throws BadRequestException if trying to remove last role
     */
    @Override
    @Transactional
    public MessageResponse removeRoleFromUser(Long userId, String roleName) {
        User user = userRepository.findByIdWithRoles(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getRoles().size() == 1) {
            throw new BadRequestException("Cannot remove the last role from user");
        }

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName));

        user.getRoles().remove(role);
        userRepository.save(user);

        log.info("Removed role {} from user: {}", roleName, user.getEmail());
        return new MessageResponse("Role removed successfully");
    }

    /**
     * Enable user account.
     *
     * @param userId user identifier
     * @return success message
     */
    @Override
    @Transactional
    public MessageResponse enableUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setEnabled(true);
        userRepository.save(user);

        log.info("User enabled: {}", user.getEmail());
        return new MessageResponse("User enabled successfully");
    }

    /**
     * Disable user account.
     *
     * @param userId user identifier
     * @return success message
     */
    @Override
    @Transactional
    public MessageResponse disableUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setEnabled(false);
        userRepository.save(user);

        log.info("User disabled: {}", user.getEmail());
        return new MessageResponse("User disabled successfully");
    }
}