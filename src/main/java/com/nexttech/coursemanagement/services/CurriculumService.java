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
        CurriculumResponseDTO curriculumResponseDTO;
        List<LessonDTO> lessonDTOList = new ArrayList<>();
        Course course = courseService.getCourse(curriculumCreationDTO.getCourseId());
        List<Long> lessonIdList = curriculumCreationDTO.getLessonIdList();
        lessonIdList.forEach(lessonId -> {
            Lesson lesson = lessonService.getLesson(lessonId);
            Curriculum newCurriculum = new Curriculum(course, lesson);
            curriculumRepo.save(newCurriculum);
            lessonDTOList.add(lessonMapper.toDto(lesson));
        });
        curriculumResponseDTO = new CurriculumResponseDTO(course.getId(), course.getCourseName(), lessonDTOList);
        return curriculumResponseDTO;
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

    public void deleteCurriculum(Long courseId) {
        //TODO: on deleting a curriculum, automatically delete course also - confirm
        curriculumRepo.findAllDistinctByCourse_Id(courseId).forEach(curriculum -> curriculumRepo.delete(curriculum));
        courseService.deleteCourse(courseId);
    }

//    public void deleteLessonFromCurriculum(Long lessonId) {
//
//    }

    //    public CourseDTO addLessonsToCourse(CurriculumCreationDTO curriculumCreationDTO) {
//        List<Lesson> lessonList = new ArrayList<>();
//        List<LessonDTO> lessonDTOList = new ArrayList<>();
//        CourseDTO addLessonsToCourseResponse = new CourseDTO();
//
//        curriculumCreationDTO.getLessonIdList().forEach(id -> {
//            Optional<Lesson> lesson = lessonRepo.findById(id);
//            if (lesson.isPresent()) {
//                LessonDTO lessonDto = lessonMapper.toDto(lesson.get());
//                lessonList.add(lesson.get());
//                lessonDTOList.add(lessonDto);
//            } else {
//                //TODO: create lesson and then add it to course -> done by FE
//            }
//        });
//        Course course = courseRepo.findById(curriculumCreationDTO.getCourseId()).get();
//        if (course != null) {
//            //TODO: save lessons to course...? or in curriculum table?
////            -> add to curriculum
////            course.setLessons(lessonList);
//            addLessonsToCourseResponse = new CourseDTO(course.getId(),course.getCourseName(), course.getUser().getId(), lessonDTOList);
//        }
//        return addLessonsToCourseResponse;
//    }
}
