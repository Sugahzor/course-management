package com.nexttech.coursemanagement.mappers;

import com.nexttech.coursemanagement.DTOs.CourseResponseDTO;
import com.nexttech.coursemanagement.models.Course;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

    public CourseResponseDTO toCreateCourseDto(Course course) {
        return new CourseResponseDTO(course.getId(), course.getCourseName(), course.getImageUrl());
    }
}
