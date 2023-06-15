package com.nexttech.coursemanagement.services;

import com.nexttech.coursemanagement.DTOs.AttendanceResponseDTO;
import com.nexttech.coursemanagement.mappers.AttendanceMapper;
import com.nexttech.coursemanagement.models.Attendance;
import com.nexttech.coursemanagement.models.Curriculum;
import com.nexttech.coursemanagement.models.User;
import com.nexttech.coursemanagement.repositories.AttendanceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class AttendanceService {

    @Autowired
    AttendanceRepo attendanceRepo;
    @Autowired
    UserService userService;
    @Autowired
    CurriculumService curriculumService;
    @Autowired
    AttendanceMapper attendanceMapper;

    public void addAttendance(Long userId, Long courseId, Long lessonId) {
        try {
            Assert.notNull(userId, "User id cannot be null.");
            Assert.notNull(courseId, "Course id cannot be null.");
            Assert.notNull(lessonId, "Lesson id cannot be null.");
            User user = userService.getUserById(userId);
            Curriculum curriculum = curriculumService.getCurriculum(courseId, lessonId);
            attendanceRepo.save(new Attendance(user, curriculum));
        }
        catch(IllegalArgumentException exception) {
            System.out.println("IllegalArgumentException caught ok");
            throw exception;
        }
    }

    public AttendanceResponseDTO getAttendanceByCurriculumAndUser(Long curriculumId, Long userId) {
        try {
            Assert.notNull(curriculumId, "Curriculum id cannot be null.");
            Assert.notNull(userId, "User id cannot be null.");
            return attendanceMapper.toDto(attendanceRepo.findByCurriculum_IdAndUser_Id(curriculumId, userId));
        }
        catch(IllegalArgumentException exception) {
            System.out.println("IllegalArgumentException caught ok");
            throw exception;
        }
    }

    public void removeAttendances(Long courseId, Long userId) {
        try {
            Assert.notNull(courseId, "Course id cannot be null.");
            Assert.notNull(userId, "User id cannot be null.");
            List<Curriculum> curriculumList = curriculumService.getAllByCourseId(courseId);
            curriculumList.forEach(curriculum -> attendanceRepo.deleteByCurriculum_IdAndUser_Id(curriculum.getId(), userId));
        }
        catch(IllegalArgumentException exception) {
            System.out.println("IllegalArgumentException caught ok");
            throw exception;
        }
    }

    @Transactional
    public void removeAttendances(Long curriculumId) {
        try {
            Assert.notNull(curriculumId, "Curriculum id cannot be null.");
            attendanceRepo.deleteByCurriculum_Id(curriculumId);
        }
        catch(IllegalArgumentException exception) {
            System.out.println("IllegalArgumentException caught ok");
            throw exception;
        }
    }
}