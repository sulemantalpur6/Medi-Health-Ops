package com.mh.auth.service.service;

import com.mh.auth.service.dto.request.AssignRoleRequest;
import com.mh.auth.service.dto.respone.MessageResponse;
import com.mh.auth.service.dto.respone.UserRolePermissionResponse;

import java.util.List;

public interface UserManagementService {

    UserRolePermissionResponse getUserRolesAndPermissions(Long userId);

    List<UserRolePermissionResponse> getAllUsersWithRoles();

    MessageResponse assignRolesToUser(AssignRoleRequest request);

    MessageResponse addRoleToUser(Long userId, String roleName);

    MessageResponse removeRoleFromUser(Long userId, String roleName);

    MessageResponse enableUser(Long userId);

    MessageResponse disableUser(Long userId);
}
