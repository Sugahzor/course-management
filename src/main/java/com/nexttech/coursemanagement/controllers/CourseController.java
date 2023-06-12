package com.nexttech.coursemanagement.controllers;

import com.nexttech.coursemanagement.DTOs.CourseCreationDTO;
import com.nexttech.coursemanagement.DTOs.CourseDTO;
import com.nexttech.coursemanagement.mappers.CourseMapper;
import com.nexttech.coursemanagement.services.CourseService;
import com.nexttech.coursemanagement.util.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private CourseMapper courseMapper;

    @GetMapping
    List<CourseDTO> getCourses() {
        List<CourseDTO> courseListResponse = courseService.getCourses();
        for (final CourseDTO course : courseListResponse) {
            Link selfLink = linkTo(CourseController.class).slash(course.getId()).withSelfRel();
            course.add(selfLink);
        }
        return courseListResponse;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public CourseDTO getById(@PathVariable("id") Long id) {
        CourseDTO courseDTO = courseMapper.toDto(courseService.getCourse(id));
        Link selfLink = linkTo(CourseController.class).slash(courseDTO.getId()).withSelfRel();
        courseDTO.add(selfLink);
        return courseDTO;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    CourseDTO addCourse(@RequestBody CourseCreationDTO courseCreationDTO) throws BadRequestException {
        CourseDTO courseResponse = courseService.addCourse(courseCreationDTO.getName(), courseCreationDTO.getImageUrl(), courseCreationDTO.getUserId());
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
