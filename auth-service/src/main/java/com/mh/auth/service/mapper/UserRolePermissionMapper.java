package com.mh.auth.service.mapper;

import com.mh.auth.service.dto.respone.UserRolePermissionResponse;
import com.mh.auth.service.model.Permission;
import com.mh.auth.service.model.Role;
import com.mh.auth.service.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserRolePermissionMapper {

    @Mapping(target = "userId", source = "id")
    @Mapping(target = "roles", expression = "java(mapRoleNames(user))")
    @Mapping(target = "permissions", expression = "java(mapPermissionNames(user))")
    UserRolePermissionResponse toUserRolePermissionResponse(User user);

    default Set<String> mapRoleNames(User user) {
        if (user == null || user.getRoles() == null) {
            return Set.of();
        }
        return user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }

    default Set<String> mapPermissionNames(User user) {
        if (user == null) {
            return Set.of();
        }
        return user.getAllPermissions().stream()
                .map(Permission::getName)
                .collect(Collectors.toSet());
    }
}
