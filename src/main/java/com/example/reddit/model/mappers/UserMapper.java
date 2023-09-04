package com.example.reddit.model.mappers;

import com.example.reddit.model.User;
import com.example.reddit.model.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toUserDTO(User user);

}
