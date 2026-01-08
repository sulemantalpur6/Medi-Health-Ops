package com.mh.auth.service.mapper;


import com.mh.auth.service.dto.respone.UserInfoResponse;
import com.mh.auth.service.model.Permission;
import com.mh.auth.service.model.Role;
import com.mh.auth.service.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", expression = "java(mapRoles(user))")
    @Mapping(target = "permissions", expression = "java(mapPermissions(user))")
    @Mapping(target = "firstName", ignore = true)
    @Mapping(target = "lastName", ignore = true)
    UserInfoResponse toUserInfoResponse(User user);

    default Set<String> mapRoles(User user) {
        if (user == null || user.getRoles() == null) {
            return Set.of();
        }
        return user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }

    default Set<String> mapPermissions(User user) {
        if (user == null) {
            return Set.of();
        }
        return user.getAllPermissions().stream()
                .map(Permission::getName)
                .collect(Collectors.toSet());
    }
}