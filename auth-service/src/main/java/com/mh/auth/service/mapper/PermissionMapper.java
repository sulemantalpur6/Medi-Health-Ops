package com.mh.auth.service.mapper;

import com.mh.auth.service.dto.respone.PermissionResponse;
import com.mh.auth.service.model.Permission;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    PermissionResponse toPermissionResponse(Permission permission);

    Set<PermissionResponse> toPermissionResponseSet(Set<Permission> permissions);

    List<PermissionResponse> toPermissionResponseList(List<Permission> permissions);
}
