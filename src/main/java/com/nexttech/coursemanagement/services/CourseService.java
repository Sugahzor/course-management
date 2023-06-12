package com.nexttech.coursemanagement.services;

import com.nexttech.coursemanagement.DTOs.CourseDTO;
import com.nexttech.coursemanagement.mappers.CourseMapper;
import com.nexttech.coursemanagement.models.Course;
import com.nexttech.coursemanagement.models.User;
import com.nexttech.coursemanagement.repositories.CourseRepo;
import com.nexttech.coursemanagement.util.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    @Autowired
    private CourseRepo courseRepo;
    @Autowired @Lazy
    private UserService userService;
    @Autowired
    private CourseMapper courseMapper;

    public CourseDTO addCourse(String name, String imgUrl, Long userId) throws BadRequestException {
        try {
            Assert.hasLength(name, "Please provide course name.");
            Course existingCourse = courseRepo.findByCourseName(name);
            User existingUser = userService.getUserById(userId);
            if (existingCourse == null && existingUser != null) {
                Course newCourse = new Course(name, imgUrl, existingUser);
                courseRepo.save(newCourse);
                return courseMapper.toDto(newCourse);
            } else {
                throw new BadRequestException("Course already exists.");
            }
        }
        catch(IllegalArgumentException exception) {
            System.out.println("IllegalArgumentException caught ok");
            throw exception;
        }
        catch(BadRequestException exception) {
            throw new BadRequestException(exception.getMessage());
        }
    }

    public Course getCourse(Long id) {
        try {
            Optional<Course> course = courseRepo.findById(id);
            Assert.isTrue(course.isPresent(), "Course not found.");
            return course.get();
        }
        catch(IllegalArgumentException exception) {
            System.out.println("IllegalArgumentException caught ok");
            throw exception;
        }
    }

    public List<CourseDTO> getCourses() {
        List<CourseDTO> courseList = new ArrayList<>();
        courseRepo.findAll().forEach(course -> {
            CourseDTO courseResponse = courseMapper.toDto(course);
            courseList.add(courseResponse);
        });
        return courseList;
    }

    public List<CourseDTO> getCoursesBySearchTerm(Optional<String> searchTerm) {
        if (searchTerm.isEmpty()) {
            return this.getCourses();
        }
        List<CourseDTO> courseDTOList = new ArrayList<>();
        courseRepo.findByCourseNameContainingIgnoreCase(searchTerm.get()).forEach(course -> courseDTOList.add(courseMapper.toDto(course)));
        return courseDTOList;
    }

    public void deleteCourse(Long id) {
        try {
            Optional<Course> course = courseRepo.findById(id);
            Assert.isTrue(course.isPresent(), "Course not found.");
            courseRepo.delete(course.get());
        }
        catch(IllegalArgumentException exception) {
            System.out.println("IllegalArgumentException caught ok");
            throw exception;
        }
    }
}
