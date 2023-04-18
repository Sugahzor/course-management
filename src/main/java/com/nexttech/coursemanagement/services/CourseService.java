package com.nexttech.coursemanagement.services;

import com.nexttech.coursemanagement.DTOs.CourseDTO;
import com.nexttech.coursemanagement.DTOs.UserDTO;
import com.nexttech.coursemanagement.mappers.CourseMapper;
import com.nexttech.coursemanagement.mappers.UserMapper;
import com.nexttech.coursemanagement.models.Course;
import com.nexttech.coursemanagement.models.User;
import com.nexttech.coursemanagement.repositories.CourseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    @Autowired
    private CourseRepo courseRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private UserMapper userMapper;

    public CourseDTO addCourse(String name, Long userId) {
        //TODO: accept lessons array
        Course existingCourse = courseRepo.findByCourseName(name);
        User existingUser = userService.getUserById(userId);
        if (existingCourse == null && existingUser != null) {
            Course newCourse = new Course(name, existingUser);
            courseRepo.save(newCourse);
            CourseDTO courseResponse = courseMapper.toDto(newCourse);
            return courseResponse;
        } else {
            System.out.println("Course already exists.");
            return null;
        }
    }

    public Course getCourse(Long id) {
        return courseRepo.findById(id).get();
    }

    public List<CourseDTO> getCourses() {
        List<CourseDTO> courseList = new ArrayList<>();
        courseRepo.findAll().forEach(course -> {
            //TODO: add lessons list
            CourseDTO courseResponse = courseMapper.toDto(course);
            courseList.add(courseResponse);
        });
        return courseList;
    }

    public void deleteCourse(Long id) {
        Optional<Course> existingCourse = courseRepo.findById(id);
        if (existingCourse.isPresent()) {
            courseRepo.delete(existingCourse.get());
        } else {
            //throw bad id error
        }

    }
}
