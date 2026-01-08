package com.mh.auth.service.dto.respone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRolePermissionResponse {

    private Long userId;
    private String email;
    private Set<String> roles;
    private Set<String> permissions;
}