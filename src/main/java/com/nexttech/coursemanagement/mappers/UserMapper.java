package com.nexttech.coursemanagement.mappers;

import com.nexttech.coursemanagement.DTOs.UserCreationDTO;
import com.nexttech.coursemanagement.DTOs.UserDTO;
import com.nexttech.coursemanagement.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDTO toDto(User user) {
        return new UserDTO(user.getId(), user.getUserName(), user.getUserEmail(), user.getUserRole());
    }

    public User toUser(UserCreationDTO userDto) {
        return new User(userDto.getUserName(), userDto.getUserEmail(), userDto.getUserPassword(), userDto.getUserRole());
    }

}
