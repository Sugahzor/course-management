package com.nexttech.coursemanagement.controllers;

import com.nexttech.coursemanagement.DTOs.*;
import com.nexttech.coursemanagement.services.CurriculumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("api/v1/curricula")
public class CurriculumController {

    @Autowired
    private CurriculumService curriculumService;

    @GetMapping
    @ResponseBody
    public List<CurriculumResponseDTO> getCurricula()    {
        List<CurriculumResponseDTO> curriculumResponseDTOList = curriculumService.getCurricula();
        for(final CurriculumResponseDTO curriculumResponseDTO: curriculumResponseDTOList) {
            Link selfLink = linkTo(CurriculumController.class).slash(curriculumResponseDTO.getCourseId()).withSelfRel();
            curriculumResponseDTO.add(selfLink);
        }
        return curriculumResponseDTOList;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public CurriculumResponseDTO getCurriculum(@PathVariable("id") Long courseId) {
        CurriculumResponseDTO curriculumResponseDTO = curriculumService.getCurriculum(courseId);
        Link selfLink = linkTo(CurriculumController.class).slash(curriculumResponseDTO.getCourseId()).withSelfRel();
        curriculumResponseDTO.add(selfLink);
        return curriculumResponseDTO;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public CurriculumResponseDTO addCurriculum(@RequestBody CurriculumCreationDTO curriculumCreationDTO) {
        return curriculumService.addCurriculum(curriculumCreationDTO);
    }

//    @PutMapping
//    @ResponseStatus(HttpStatus.OK)
//    CourseDTO addLessonsToCourse(@RequestBody CurriculumCreationDTO curriculumCreationDTO) {
//        CourseDTO courseWithLessonsResponse = courseService.addLessonsToCourse(curriculumCreationDTO);
//        return courseWithLessonsResponse;
//    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCurriculum(@PathVariable("id") Long courseId) {
        curriculumService.deleteCurriculum(courseId);
    }

}
