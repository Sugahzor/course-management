package com.nexttech.coursemanagement.mappers;

import com.nexttech.coursemanagement.DTOs.AttendanceResponseDTO;
import com.nexttech.coursemanagement.models.Attendance;
import org.springframework.stereotype.Component;

@Component
public class AttendanceMapper {
    public AttendanceResponseDTO toDto(Attendance attendance) {
        return new AttendanceResponseDTO(attendance.getId(), attendance.getAttendanceGrade());
    }
}