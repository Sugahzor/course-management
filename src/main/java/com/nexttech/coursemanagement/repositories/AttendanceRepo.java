package com.nexttech.coursemanagement.repositories;

import com.nexttech.coursemanagement.models.Attendance;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepo extends CrudRepository<Attendance, Long> {
    Attendance findByCurriculum_IdAndUser_Id(Long curriculumId, Long userId);
    void deleteByCurriculum_IdAndUser_Id(Long curriculumId, Long userId);
    void deleteByCurriculum_Id(Long curriculumId);
}