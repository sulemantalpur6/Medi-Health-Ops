package com.hm.user.service.mapper;

import com.hm.user.service.dto.UserDTO;
import com.hm.user.service.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO userToUserDTO(User user);

    User userDTOToUser(UserDTO userDTO);
}
