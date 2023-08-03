package com.nexttech.coursemanagement.controllers;

import com.nexttech.coursemanagement.DTOs.CourseCreationDTO;
import com.nexttech.coursemanagement.DTOs.CourseResponseDTO;
import com.nexttech.coursemanagement.DTOs.CurriculumCreationDTO;
import com.nexttech.coursemanagement.models.AppUserPrincipal;
import com.nexttech.coursemanagement.services.CourseService;
import com.nexttech.coursemanagement.util.BadRequestException;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping
    List<CourseResponseDTO> getCourses(@RequestParam(required=false) Optional<String> searchTerm) {
        List<CourseResponseDTO> courseListResponse = courseService.getCoursesBySearchTerm(searchTerm);
        for (final CourseResponseDTO course : courseListResponse) {
            Link selfLink = linkTo(CourseController.class).slash(course.getCourseId()).withSelfRel();
            course.add(selfLink);
        }
        return courseListResponse;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public CourseResponseDTO getById(@PathVariable("id") Long id) {
        CourseResponseDTO courseResponseDTO = courseService.getCourseResponse(id);
        Link selfLink = linkTo(CourseController.class).slash(courseResponseDTO.getCourseId()).withSelfRel();
        courseResponseDTO.add(selfLink);
        return courseResponseDTO;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    CourseResponseDTO addCourse(@RequestBody CourseCreationDTO courseCreationDTO, @Parameter(hidden = true) @AuthenticationPrincipal AppUserPrincipal principal) throws BadRequestException {
        CourseResponseDTO courseResponse = courseService.addCourse(courseCreationDTO.getName(), courseCreationDTO.getImageUrl(), principal.getUsername());
        Link selfLink = linkTo(CourseController.class).slash(courseResponse.getCourseId()).withSelfRel();
        courseResponse.add(selfLink);
        return courseResponse;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    CourseResponseDTO addCourseCurriculum(@RequestBody CurriculumCreationDTO curriculumCreationDTO) {
        //Add lessons to course
        return courseService.addCourseCurriculum(curriculumCreationDTO);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCourse(@PathVariable("id") Long courseId) {
        courseService.deleteCourse(courseId);
    }

    @DeleteMapping(value = "/{courseId}/lessonId/{lessonId}")
    @ResponseStatus(HttpStatus.OK)
    public CourseResponseDTO deleteLessonFromCourse(@PathVariable("courseId") Long courseId, @PathVariable("lessonId") Long lessonId) {
        return courseService.deleteLessonFromCourse(courseId, lessonId);
    }
}
