package com.hm.user.service.service.impl;

import com.hm.user.service.dto.UserDTO;
import com.hm.user.service.mapper.UserMapper;
import com.hm.user.service.repository.UserRepository;
import com.hm.user.service.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        return userMapper.userToUserDTO(userRepository.save(userMapper.userDTOToUser(userDTO)));
    }
}
