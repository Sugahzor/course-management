package com.nexttech.coursemanagement.services;

import com.nexttech.coursemanagement.DTOs.CourseResponseDTO;
import com.nexttech.coursemanagement.mappers.CourseMapper;
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
    private CourseMapper mapper;

    public CourseResponseDTO addCourse(String name, Long userId) {
        Course existingCourse = courseRepo.findByCourseName(name);
        User existingUser = userService.getUserById(userId);
        if (existingCourse == null && existingUser != null) {
            Course newCourse = new Course(name, existingUser);
            courseRepo.save(newCourse);
            CourseResponseDTO courseResponse = mapper.toDto(newCourse);
            return courseResponse;
        } else {
            System.out.println("Course already exists.");
            return null;
        }
    }

    public List<Course> getCourses() {
        List<Course> courseList = new ArrayList<>();
        courseRepo.findAll().forEach(course -> courseList.add(course));
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
