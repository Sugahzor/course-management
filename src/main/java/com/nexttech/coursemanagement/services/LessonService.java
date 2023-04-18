package com.nexttech.coursemanagement.services;

import com.nexttech.coursemanagement.DTOs.LessonCreationDTO;
import com.nexttech.coursemanagement.DTOs.LessonDTO;
import com.nexttech.coursemanagement.mappers.LessonMapper;
import com.nexttech.coursemanagement.models.Lesson;
import com.nexttech.coursemanagement.models.User;
import com.nexttech.coursemanagement.repositories.LessonRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LessonService {
    @Autowired
    private LessonRepo lessonRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private LessonMapper lessonMapper;

    public LessonDTO addLesson(LessonCreationDTO lessonCreationDTO) {
        User user = userService.getUserById(lessonCreationDTO.getUserId());
        Lesson lesson = new Lesson(lessonCreationDTO.getName(), lessonCreationDTO.getContent(), user);
        lessonRepo.save(lesson);
        LessonDTO lessonResponse = lessonMapper.toDto(lesson);
        return lessonResponse;
    }

    public Lesson getLesson(Long id) {
        return lessonRepo.findById(id).get();
    }

    public List<LessonDTO> getLessons() {
        List<LessonDTO> lessonsList = new ArrayList<>();
        lessonRepo.findAll().forEach(lesson -> {
            LessonDTO lessonResponse = lessonMapper.toDto(lesson);
            lessonsList.add(lessonResponse);
        });
        return lessonsList;
    }

    public void deleteLesson(Long id) {
        lessonRepo.deleteById(id);
    }
}
