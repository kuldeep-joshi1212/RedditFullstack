package com.example.reddit.model.mappers.impl;

import com.example.reddit.model.User;
import com.example.reddit.model.dto.UserDTO;
import com.example.reddit.model.mappers.UserMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public UserDTO toUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setToken(user.getToken());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }
}
