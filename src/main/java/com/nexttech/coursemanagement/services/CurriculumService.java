package com.nexttech.coursemanagement.services;

import com.nexttech.coursemanagement.DTOs.*;
import com.nexttech.coursemanagement.mappers.LessonMapper;
import com.nexttech.coursemanagement.models.*;
import com.nexttech.coursemanagement.repositories.CurriculumRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

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
        try {
            List<LessonDTO> lessonDTOList = new ArrayList<>();
            Course course = courseService.getCourse(curriculumCreationDTO.getCourseId());
            Assert.notNull(course, "Course not found.");
            List<Long> lessonIdList = curriculumCreationDTO.getLessonIdList();
            lessonIdList.forEach(lessonId -> {
                Lesson lesson = lessonService.getLesson(lessonId);
                Assert.notNull(lesson, "Lesson id not ok: " + lessonId);
                Curriculum newCurriculum = new Curriculum(course, lesson);
                curriculumRepo.save(newCurriculum);
                lessonDTOList.add(lessonMapper.toDto(lesson));
            });
            return new CurriculaResponseDTO(course.getId(), course.getCourseName(), course.getImageUrl(), lessonDTOList);
        }
        catch(IllegalArgumentException exception) {
            System.out.println("IllegalArgumentException caught ok");
            throw exception;
        }
    }

    public List<Curriculum> getAllByCourseId(Long courseId) {
        try {
            Assert.notNull(courseId, "CourseId cannot be null.");
            return curriculumRepo.findAllByCourse_Id(courseId);
        }
        catch(IllegalArgumentException exception) {
            System.out.println("IllegalArgumentException caught ok");
            throw exception;
        }
    }

    public List<LessonDTO> getLessonsByCourseId(Long courseId) {
        try {
            Assert.notNull(courseId, "CourseId cannot be null.");
            List<LessonDTO> lessonDTOList = new ArrayList<>();
            curriculumRepo.findAllDistinctByCourse_Id(courseId).forEach(curriculum -> lessonDTOList.add(lessonMapper.toDto(curriculum.getLesson())));
            return lessonDTOList;
        }
        catch(IllegalArgumentException exception) {
            System.out.println("IllegalArgumentException caught ok");
            throw exception;
        }
    }

    public List<LessonDTO> getLessonsWithAttendance(Long courseId, Long userId) {
        try {
            Assert.notNull(courseId, "Course id cannot be null");
            Assert.notNull(userId, "User id cannot be null");
            List<LessonDTO> lessonDTOList = new ArrayList<>();
            curriculumRepo.findAllDistinctByCourse_Id(courseId).forEach(curriculum ->
                    lessonDTOList.add(lessonMapper.toDto(curriculum.getLesson(), attendanceService.getAttendanceByCurriculumAndUser(curriculum.getId(), userId))));
            return lessonDTOList;
        }
        catch(IllegalArgumentException exception) {
            System.out.println("IllegalArgumentException caught ok");
            throw exception;
        }
    }

    public List<CurriculaResponseDTO> getCurricula() {
        List<CurriculaResponseDTO> curriculaResponse = new ArrayList<>();
        List<CourseDTO> courses = new ArrayList<>();
        //TODO: Warning:(53, 54) Iteration can be replaced with bulk 'Collection.addAll()' call
//        Collections.addAll(courses, courseService.getCourses()); -> needs individual elements...?
        courseService.getCourses().forEach(course -> courses.add(course));
        courses.forEach(course -> curriculaResponse.add(new CurriculaResponseDTO(course.getId(), course.getName(), course.getImageUrl(), getLessonsByCourseId(course.getId()))));
        return curriculaResponse;
    }

    public CurriculaResponseDTO getCurricula(Long courseId) {
        try {
            Assert.notNull(courseId, "Course id cannot be null.");
            Course course = courseService.getCourse(courseId);
            return new CurriculaResponseDTO(courseId, course.getCourseName(), course.getImageUrl(), getLessonsByCourseId(courseId));
        }
        catch(IllegalArgumentException exception) {
            System.out.println("IllegalArgumentException caught ok");
            throw exception;
        }
    }

    public List<CurriculaResponseDTO> getCurriculaByUser(Long userId) {
        try {
            Assert.notNull(userId, "User id cannot be null.");
            List<CurriculaResponseDTO> curriculaResponse = new ArrayList<>();
            User user = userService.getUserById(userId);
            Set<Course> userCourses = user.getCourses();
            Assert.notNull(userCourses, "User has no courses.");
            userCourses.forEach(course ->
                    curriculaResponse.add(new CurriculaResponseDTO(course.getId(), course.getCourseName(), course.getImageUrl(), getLessonsWithAttendance(course.getId(), userId)))
            );
            return curriculaResponse;
        }
        catch(IllegalArgumentException exception) {
            System.out.println("IllegalArgumentException caught ok");
            throw exception;
        }
    }

    public Curriculum getCurriculum(Long courseId, Long lessonId) {
        try {
            Assert.notNull(courseId, "Course id cannot be null.");
            Assert.notNull(lessonId, "Lesson id cannot be null.");
            return curriculumRepo.findByCourse_IdAndLesson_Id(courseId,lessonId);
        }
        catch(IllegalArgumentException exception) {
            System.out.println("IllegalArgumentException caught ok");
            throw exception;
        }
    }

    public void deleteCurricula(Long courseId) {
        try {
            Assert.notNull(courseId, "Course id cannot be null.");
            //TODO: on deleting a curriculum, automatically delete course also - confirm
            //TODO: also delete attendance/homework for this curricula for all users enrolled to course, correct?
            curriculumRepo.findAllDistinctByCourse_Id(courseId).forEach(curriculum -> {
                attendanceService.removeAttendances(curriculum.getId());
                curriculumRepo.delete(curriculum);
            });
            courseService.deleteCourse(courseId);
        }
        catch(IllegalArgumentException exception) {
            System.out.println("IllegalArgumentException caught ok");
            throw exception;
        }
    }

    public void deleteCurriculum(Long courseId, Long lessonId) {
        try {
            Assert.notNull(courseId, "Course id cannot be null.");
            Assert.notNull(lessonId, "Lesson id cannot be null.");
            //TODO: also delete attendance/homework for this curriculum for all users enrolled to course, correct?
            Long curriculumId = (curriculumRepo.findByCourse_IdAndLesson_Id(courseId, lessonId).getId());
            attendanceService.removeAttendances(curriculumId);
            curriculumRepo.deleteByCourse_IdAndLesson_Id(courseId, lessonId);
        }
        catch(IllegalArgumentException exception) {
            System.out.println("IllegalArgumentException caught ok");
            throw exception;
        }
    }
}
