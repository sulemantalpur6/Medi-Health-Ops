package com.mh.auth.service.mapper;


import com.mh.auth.service.dto.respone.RoleResponse;
import com.mh.auth.service.model.Role;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {PermissionMapper.class})
public interface RoleMapper {

    RoleResponse toRoleResponse(Role role);

    List<RoleResponse> toRoleResponseList(List<Role> roles);
}
