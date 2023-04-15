package com.nexttech.coursemanagement.services;

import com.nexttech.coursemanagement.models.Course;
import com.nexttech.coursemanagement.models.Curriculum;
import com.nexttech.coursemanagement.models.Lesson;
import com.nexttech.coursemanagement.repositories.CurriculumRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CurriculumService {
    @Autowired
    private CurriculumRepo curriculumRepo;
    @Autowired CourseService courseService;
    @Autowired
    private LessonService lessonService;

    public void addCurriculum(Long courseId, Long lessonId) {
        Course course = courseService.getCourse(courseId);
        Lesson lesson = lessonService.getLesson(lessonId);
        Curriculum newCurriculum = new Curriculum(course, lesson);
        curriculumRepo.save(newCurriculum);
    }

    public List<Curriculum> getCurricula() {
        List<Curriculum> curricula = new ArrayList<>();
        curriculumRepo.findAll().forEach(curriculum -> curricula.add(curriculum));
        return curricula;
    }
}
