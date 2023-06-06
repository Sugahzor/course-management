package com.nexttech.coursemanagement.mappers;

import com.nexttech.coursemanagement.DTOs.AttendanceResponseDTO;
import com.nexttech.coursemanagement.DTOs.LessonDTO;
import com.nexttech.coursemanagement.models.Lesson;
import org.springframework.stereotype.Component;

@Component
public class LessonMapper {
    public LessonDTO toDto(Lesson lesson) {
        return new LessonDTO(lesson.getId(), lesson.getName(), lesson.getContent(), lesson.getUser().getId());
    }

    public LessonDTO toDto(Lesson lesson, AttendanceResponseDTO attendanceResponseDTO) {
        return new LessonDTO(lesson.getId(), lesson.getName(), lesson.getContent(), lesson.getUser().getId(), attendanceResponseDTO);
    }
}
