package com.nexttech.coursemanagement.services;

import com.nexttech.coursemanagement.DTOs.AttendanceResponseDTO;
import com.nexttech.coursemanagement.mappers.AttendanceMapper;
import com.nexttech.coursemanagement.models.Attendance;
import com.nexttech.coursemanagement.models.Curriculum;
import com.nexttech.coursemanagement.models.User;
import com.nexttech.coursemanagement.repositories.AttendanceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

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

    @Autowired @Lazy
    CourseService courseService;

    public void addAttendance(Long userId, Long courseId, Long lessonId) {
        User user = userService.getUserById(userId);
        Curriculum curriculum = curriculumService.getCurriculum(courseId, lessonId);
        attendanceRepo.save(new Attendance(user, curriculum));
    }

    public AttendanceResponseDTO getAttendanceByCurriculumAndUser(Long curriculumId, Long userId) {
        return attendanceMapper.toDto(attendanceRepo.findByCurriculum_IdAndUser_Id(curriculumId, userId));
    }

    public void removeAttendances(Long courseId, Long userId) {
        List<Curriculum> curriculumList = curriculumService.getAllByCourseId(courseId);
        curriculumList.forEach(curriculum -> attendanceRepo.deleteByCurriculum_IdAndUser_Id(curriculum.getId(), userId));
    }

    public void removeAttendances(Long curriculumId) {
        attendanceRepo.deleteByCurriculum_Id(curriculumId);
    }
}