package com.nexttech.coursemanagement.services;

import com.nexttech.coursemanagement.DTOs.*;
import com.nexttech.coursemanagement.mappers.LessonMapper;
import com.nexttech.coursemanagement.models.Course;
import com.nexttech.coursemanagement.models.Curriculum;
import com.nexttech.coursemanagement.models.Lesson;
import com.nexttech.coursemanagement.repositories.CurriculumRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CurriculumService {
    @Autowired
    private CurriculumRepo curriculumRepo;
    @Autowired
    private CourseService courseService;
    @Autowired
    private LessonService lessonService;
    @Autowired
    private LessonMapper lessonMapper;


    public CurriculumResponseDTO addCurriculum(CurriculumCreationDTO curriculumCreationDTO) {
        List<LessonDTO> lessonDTOList = new ArrayList<>();
        Course course = courseService.getCourse(curriculumCreationDTO.getCourseId());
        List<Long> lessonIdList = curriculumCreationDTO.getLessonIdList();
        lessonIdList.forEach(lessonId -> {
            Lesson lesson = lessonService.getLesson(lessonId);
            Curriculum newCurriculum = new Curriculum(course, lesson);
            curriculumRepo.save(newCurriculum);
            lessonDTOList.add(lessonMapper.toDto(lesson));
        });
        return new CurriculumResponseDTO(course.getId(), course.getCourseName(), lessonDTOList);
    }

    public List<LessonDTO> getLessonsByCourseId(Long courseId) {
        List<LessonDTO> lessonDTOList = new ArrayList<>();
        curriculumRepo.findAllDistinctByCourse_Id(courseId).forEach(curriculum -> lessonDTOList.add(lessonMapper.toDto(curriculum.getLesson())));
        return lessonDTOList;
    }

    public List<CurriculumResponseDTO> getCurricula() {
        List<CurriculumResponseDTO> curriculaResponse = new ArrayList<>();
        List<CourseDTO> courses = new ArrayList<>();
        //TODO: Warning:(53, 54) Iteration can be replaced with bulk 'Collection.addAll()' call
        courseService.getCourses().forEach(course -> courses.add(course));
        courses.forEach(course -> curriculaResponse.add(new CurriculumResponseDTO(course.getId(), course.getName(), getLessonsByCourseId(course.getId()))));
        return curriculaResponse;
    }

    public CurriculumResponseDTO getCurriculum(Long courseId) {
        Course course = courseService.getCourse(courseId);
        return new CurriculumResponseDTO(courseId, course.getCourseName(), getLessonsByCourseId(courseId));
    }

    public Curriculum getCurriculumId(Long courseId, Long lessonId) {
        System.out.println(curriculumRepo.findByCourse_IdAndLesson_Id(courseId,lessonId).getCourse().getCourseName());
        return curriculumRepo.findByCourse_IdAndLesson_Id(courseId,lessonId);
    }

    public void deleteCurricula(Long courseId) {
        //TODO: on deleting a curriculum, automatically delete course also - confirm
        curriculumRepo.findAllDistinctByCourse_Id(courseId).forEach(curriculum -> curriculumRepo.delete(curriculum));
        courseService.deleteCourse(courseId);
    }

    public void deleteCurriculum(Long courseId, Long lessonId) {
        curriculumRepo.deleteById(curriculumRepo.findByCourse_IdAndLesson_Id(courseId, lessonId).getId());
//        curriculumRepo.deleteByCourse_IdAndLesson_Id(courseId, lessonId);
    }
}
