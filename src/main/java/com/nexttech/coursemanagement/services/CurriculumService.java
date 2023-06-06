package com.nexttech.coursemanagement.services;

import com.nexttech.coursemanagement.DTOs.*;
import com.nexttech.coursemanagement.mappers.LessonMapper;
import com.nexttech.coursemanagement.models.*;
import com.nexttech.coursemanagement.repositories.CurriculumRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
    @Autowired @Lazy
    private UserService userService;
    @Autowired @Lazy
    AttendanceService attendanceService;


    public CurriculaResponseDTO addCurriculum(CurriculumCreationDTO curriculumCreationDTO) {
        List<LessonDTO> lessonDTOList = new ArrayList<>();
        Course course = courseService.getCourse(curriculumCreationDTO.getCourseId());
        List<Long> lessonIdList = curriculumCreationDTO.getLessonIdList();
        lessonIdList.forEach(lessonId -> {
            Lesson lesson = lessonService.getLesson(lessonId);
            Curriculum newCurriculum = new Curriculum(course, lesson);
            curriculumRepo.save(newCurriculum);
            lessonDTOList.add(lessonMapper.toDto(lesson));
        });
        return new CurriculaResponseDTO(course.getId(), course.getCourseName(), lessonDTOList);
    }

    public List<Curriculum> getAllByCourseId(Long courseId) {
        return curriculumRepo.findAllByCourse_Id(courseId);
    }

    public List<LessonDTO> getLessonsByCourseId(Long courseId) {
        List<LessonDTO> lessonDTOList = new ArrayList<>();
        curriculumRepo.findAllDistinctByCourse_Id(courseId).forEach(curriculum -> lessonDTOList.add(lessonMapper.toDto(curriculum.getLesson())));
        return lessonDTOList;
    }

    public List<LessonDTO> getLessonsWithAttendance(Long courseId, Long userId) {
        List<LessonDTO> lessonDTOList = new ArrayList<>();
        curriculumRepo.findAllDistinctByCourse_Id(courseId).forEach(curriculum ->
                lessonDTOList.add(lessonMapper.toDto(curriculum.getLesson(), attendanceService.getAttendanceByCurriculumAndUser(curriculum.getId(), userId))));
        return lessonDTOList;
    }

    public List<CurriculaResponseDTO> getCurricula() {
        List<CurriculaResponseDTO> curriculaResponse = new ArrayList<>();
        List<CourseDTO> courses = new ArrayList<>();
        //TODO: Warning:(53, 54) Iteration can be replaced with bulk 'Collection.addAll()' call
        courseService.getCourses().forEach(course -> courses.add(course));
        courses.forEach(course -> curriculaResponse.add(new CurriculaResponseDTO(course.getId(), course.getName(), getLessonsByCourseId(course.getId()))));
        return curriculaResponse;
    }

    public CurriculaResponseDTO getCurricula(Long courseId) {
        Course course = courseService.getCourse(courseId);
        return new CurriculaResponseDTO(courseId, course.getCourseName(), getLessonsByCourseId(courseId));
    }

    public List<CurriculaResponseDTO> getCurriculaByUser(Long userId) {
        List<CurriculaResponseDTO> curriculaResponse = new ArrayList<>();
        User user = userService.getUserById(userId);
        Set<Course> userCourses = user.getCourses();
        userCourses.forEach(course ->
            curriculaResponse.add(new CurriculaResponseDTO(course.getId(), course.getCourseName(), getLessonsWithAttendance(course.getId(), userId)))
        );
        return curriculaResponse;
    }

    public Curriculum getCurriculum(Long courseId, Long lessonId) {
        return curriculumRepo.findByCourse_IdAndLesson_Id(courseId,lessonId);
    }

    public void deleteCurricula(Long courseId) {
        //TODO: on deleting a curriculum, automatically delete course also - confirm
        //TODO: also delete attendance/homework for this curricula for all users enrolled to course, correct?
        curriculumRepo.findAllDistinctByCourse_Id(courseId).forEach(curriculum -> {
            attendanceService.removeAttendances(curriculum.getId());
            curriculumRepo.delete(curriculum);
        });
        courseService.deleteCourse(courseId);
    }

    public void deleteCurriculum(Long courseId, Long lessonId) {
        //TODO: also delete attendance/homework for this curriculum for all users enrolled to course, correct?
        Long curriculumId = (curriculumRepo.findByCourse_IdAndLesson_Id(courseId, lessonId).getId());
        attendanceService.removeAttendances(curriculumId);
        curriculumRepo.deleteByCourse_IdAndLesson_Id(courseId, lessonId);
    }
}
