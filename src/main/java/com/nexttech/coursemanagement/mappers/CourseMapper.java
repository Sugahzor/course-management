package com.nexttech.coursemanagement.mappers;

import com.nexttech.coursemanagement.DTOs.CourseDTO;
import com.nexttech.coursemanagement.models.Course;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

    public CourseDTO toDto(Course course) {
        return new CourseDTO(course.getId(), course.getCourseName(), course.getImageUrl(), course.getUser().getId());
    }
}
