package com.mh.auth.service.controller;

import com.mh.auth.service.dto.request.AssignRoleRequest;
import com.mh.auth.service.dto.respone.MessageResponse;
import com.mh.auth.service.dto.respone.UserRolePermissionResponse;
import com.mh.auth.service.service.UserManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for user management operations.
 * Requires ROLE_MANAGE permission for all operations.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasAuthority('ROLE_MANAGE')")
public class UserManagementController {

    private final UserManagementService userManagementService;

    /**
     * Get all users with their roles and permissions.
     *
     * @return list of users with roles
     */
    @GetMapping
    public ResponseEntity<List<UserRolePermissionResponse>> getAllUsers() {
        log.info("Get all users endpoint called");
        List<UserRolePermissionResponse> response = userManagementService.getAllUsersWithRoles();
        return ResponseEntity.ok(response);
    }

    /**
     * Get specific user's roles and permissions.
     *
     * @param userId user identifier
     * @return user roles and permissions
     */
    @GetMapping("/{userId}/roles-permissions")
    public ResponseEntity<UserRolePermissionResponse> getUserRolesAndPermissions(@PathVariable Long userId) {
        log.info("Get user roles and permissions endpoint called for user ID: {}", userId);
        UserRolePermissionResponse response = userManagementService.getUserRolesAndPermissions(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Assign multiple roles to a user (replaces existing roles).
     *
     * @param request role assignment details
     * @return success message
     */
    @PostMapping("/assign-roles")
    public ResponseEntity<MessageResponse> assignRoles(@Valid @RequestBody AssignRoleRequest request) {
        log.info("Assign roles endpoint called for user ID: {}", request.getUserId());
        MessageResponse response = userManagementService.assignRolesToUser(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Add a single role to user's existing roles.
     *
     * @param userId user identifier
     * @param requestBody role name
     * @return success message
     */
    @PostMapping("/{userId}/add-role")
    public ResponseEntity<MessageResponse> addRole(
            @PathVariable Long userId,
            @RequestBody Map<String, String> requestBody) {
        String roleName = requestBody.get("roleName");
        log.info("Add role endpoint called for user ID: {} with role: {}", userId, roleName);
        MessageResponse response = userManagementService.addRoleToUser(userId, roleName);
        return ResponseEntity.ok(response);
    }

    /**
     * Remove a role from user.
     *
     * @param userId user identifier
     * @param requestBody role name
     * @return success message
     */
    @DeleteMapping("/{userId}/remove-role")
    public ResponseEntity<MessageResponse> removeRole(
            @PathVariable Long userId,
            @RequestBody Map<String, String> requestBody) {
        String roleName = requestBody.get("roleName");
        log.info("Remove role endpoint called for user ID: {} with role: {}", userId, roleName);
        MessageResponse response = userManagementService.removeRoleFromUser(userId, roleName);
        return ResponseEntity.ok(response);
    }

    /**
     * Enable a user account.
     *
     * @param userId user identifier
     * @return success message
     */
    @PatchMapping("/{userId}/enable")
    public ResponseEntity<MessageResponse> enableUser(@PathVariable Long userId) {
        log.info("Enable user endpoint called for user ID: {}", userId);
        MessageResponse response = userManagementService.enableUser(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Disable a user account.
     *
     * @param userId user identifier
     * @return success message
     */
    @PatchMapping("/{userId}/disable")
    public ResponseEntity<MessageResponse> disableUser(@PathVariable Long userId) {
        log.info("Disable user endpoint called for user ID: {}", userId);
        MessageResponse response = userManagementService.disableUser(userId);
        return ResponseEntity.ok(response);
    }
}
