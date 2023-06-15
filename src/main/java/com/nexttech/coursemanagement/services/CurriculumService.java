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
    private LessonMapper lessonMapper;
    @Autowired @Lazy
    AttendanceService attendanceService;

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
            curriculumRepo.findAllDistinctByCourse_Id(courseId).forEach(curriculum -> lessonDTOList.add(lessonMapper.toDtoNoAttendance(curriculum.getLesson())));
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

}
