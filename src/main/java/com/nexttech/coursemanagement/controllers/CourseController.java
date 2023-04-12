package com.nexttech.coursemanagement.controllers;

import com.nexttech.coursemanagement.DTOs.CourseCreationDTO;
//import com.nexttech.coursemanagement.DTOs.CourseResponseDTO;
import com.nexttech.coursemanagement.DTOs.CourseResponseDTO;
import com.nexttech.coursemanagement.models.Course;
import com.nexttech.coursemanagement.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping
    List<CourseResponseDTO> getCourses() {
        List<CourseResponseDTO> courseListResponse = courseService.getCourses();

        for (final CourseResponseDTO course : courseListResponse) {
            Link selfLink = linkTo(CourseController.class).slash(course.getId()).withSelfRel();
            course.add(selfLink);
        }
        return courseListResponse;
//        return courseService.getCourses();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    CourseResponseDTO addCourse(@RequestBody CourseCreationDTO courseCreationDTO) {
        CourseResponseDTO courseResponse = courseService.addCourse(courseCreationDTO.getName(), courseCreationDTO.getUserId());
            Link selfLink = linkTo(CourseController.class).slash(courseResponse.getId()).withSelfRel();
        courseResponse.add(selfLink);
        return courseResponse;
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCourse(@PathVariable("id") Long courseId) {
        courseService.deleteCourse(courseId);
    }
}
