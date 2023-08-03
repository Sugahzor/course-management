package com.nexttech.coursemanagement.services;

import com.nexttech.coursemanagement.DTOs.LessonCreationDTO;
import com.nexttech.coursemanagement.DTOs.LessonDTO;
import com.nexttech.coursemanagement.mappers.LessonMapper;
import com.nexttech.coursemanagement.models.Curriculum;
import com.nexttech.coursemanagement.models.Lesson;
import com.nexttech.coursemanagement.models.User;
import com.nexttech.coursemanagement.repositories.CurriculumRepo;
import com.nexttech.coursemanagement.repositories.LessonRepo;
import com.nexttech.coursemanagement.repositories.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
public class LessonService {
    @Autowired
    private LessonRepo lessonRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private LessonMapper lessonMapper;
    @Autowired @Lazy
    private CurriculumRepo curriculumRepo;
    @Autowired @Lazy
    private CourseService courseService;

    public LessonDTO addLesson(LessonCreationDTO lessonCreationDTO, String username) {
        try {
            User user = userRepo.findByUserName(username).orElseThrow();
            Assert.hasLength(lessonCreationDTO.getName(), "Lesson name cannot be empty");
            Assert.notNull(lessonCreationDTO.getContent(), "Lesson must have content.");
            Lesson lesson = new Lesson(lessonCreationDTO.getName(), lessonCreationDTO.getContent(), user);
            lessonRepo.save(lesson);
            LessonDTO lessonResponse = lessonMapper.toDtoNoAttendance(lesson);
            return lessonResponse;
        }
        catch (NoSuchElementException exception) {
            log.error("User not found");
            throw exception;
        }
        catch(IllegalArgumentException exception) {
            System.out.println("IllegalArgumentException caught ok");
            throw exception;
        }
    }

    public Lesson getLesson(Long id) {
        try {
            Assert.notNull(id, "Id cannot be null");
            Lesson lesson = lessonRepo.findById(id).get();
            Assert.notNull(lesson, "Lesson not found.");
            return lesson;
        }
        catch(IllegalArgumentException exception) {
            System.out.println("IllegalArgumentException caught ok");
            throw exception;
        }
    }

    public List<LessonDTO> getLessons() {
        List<LessonDTO> lessonsList = new ArrayList<>();
        lessonRepo.findAll().forEach(lesson -> {
            LessonDTO lessonResponse = lessonMapper.toDtoNoAttendance(lesson);
            lessonsList.add(lessonResponse);
        });
        return lessonsList;
    }

    public List<LessonDTO> getLessonsBySearchTerm(Optional<String> searchTerm) {
        if (searchTerm.isEmpty()) {
            return getLessons();
        }
        List<LessonDTO> lessonDTOList = new ArrayList<>();
        lessonRepo.findByNameContainingIgnoreCase(searchTerm.get()).forEach(lesson -> lessonDTOList.add(lessonMapper.toDtoNoAttendance(lesson)));
        return lessonDTOList;
    }

    public void deleteLesson(Long lessonId) {
        try {
            List<Curriculum> curriculumList = curriculumRepo.findAllByLessonId(lessonId);
            curriculumList.forEach(curriculum -> courseService.deleteCurriculumAndAttendances(curriculum));
            lessonRepo.deleteById(lessonId);
        }
        catch(IllegalArgumentException exception) {
            System.out.println("IllegalArgumentException caught ok");
            throw exception;
        }
    }
}
