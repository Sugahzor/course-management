package com.nexttech.coursemanagement.mappers;

import com.nexttech.coursemanagement.DTOs.CourseCreationDTO;
import com.nexttech.coursemanagement.DTOs.CourseResponseDTO;
import com.nexttech.coursemanagement.DTOs.UserDTO;
import com.nexttech.coursemanagement.models.Course;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {
    public CourseResponseDTO toDto(Course course, UserDTO userDTO) {
        return new CourseResponseDTO(course.getId(), userDTO);
    }
}
