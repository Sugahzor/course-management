package com.nexttech.coursemanagement.controllers;

import com.nexttech.coursemanagement.DTOs.LessonCreationDTO;
import com.nexttech.coursemanagement.DTOs.LessonDTO;
import com.nexttech.coursemanagement.mappers.LessonMapper;
import com.nexttech.coursemanagement.services.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("api/v1/lessons")
public class LessonController {
    @Autowired
    private LessonService lessonService;
    @Autowired
    private LessonMapper lessonMapper;

    @GetMapping
    @ResponseBody
    public List<LessonDTO> getLessons(@RequestParam(required=false) Optional<String> searchTerm) {
        List<LessonDTO> lessonsList = lessonService.getLessonsBySearchTerm(searchTerm);
        for(final LessonDTO lesson: lessonsList) {
            Link selfLink = linkTo(LessonController.class).slash(lesson.getId()).withSelfRel();
            lesson.add(selfLink);
        }
        return lessonsList;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public LessonDTO getById(@PathVariable("id") Long id) {
        return lessonMapper.toDtoNoAttendance(lessonService.getLesson(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    LessonDTO addLesson(@RequestBody LessonCreationDTO lessonCreationDTO) {
        LessonDTO lessonResponse = lessonService.addLesson(lessonCreationDTO);
        Link selfLink = linkTo(LessonController.class).slash(lessonResponse.getId()).withSelfRel();
        lessonResponse.add(selfLink);
        return lessonResponse;
    }

    @DeleteMapping(value = "/{id}")
    @ResponseBody
    void deleteLesson(@PathVariable("id") Long id) {
        lessonService.deleteLesson(id);
    }
}
