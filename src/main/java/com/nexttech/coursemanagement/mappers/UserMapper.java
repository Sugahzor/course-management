package com.nexttech.coursemanagement.mappers;

import com.nexttech.coursemanagement.DTOs.UserCreationDTO;
import com.nexttech.coursemanagement.DTOs.UserDTO;
import com.nexttech.coursemanagement.models.Course;
import com.nexttech.coursemanagement.models.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class UserMapper {
    public UserDTO toDto(User user) {
        ArrayList<Long> coursesEnrolledToIds = new ArrayList<>();
        for (Course course : user.getCourses()) {
            coursesEnrolledToIds.add(course.getId());
        }
        return new UserDTO(user.getId(), user.getUserName(), user.getUserEmail(), user.getUserRole(), coursesEnrolledToIds);
    }

    public User toUser(UserCreationDTO userDto) {
        return new User(userDto.getUserName(), userDto.getUserEmail(), userDto.getUserPassword(), userDto.getUserRole());
    }

}
