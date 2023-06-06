package com.nexttech.coursemanagement.DTOs;

import org.springframework.hateoas.RepresentationModel;

public class LessonDTO extends RepresentationModel<LessonDTO> {
    Long id;
    String name;
    Byte[] content;
    Long userId;
    AttendanceResponseDTO attendanceResponseDTO;

    public LessonDTO(Long id, String name, Byte[] content, Long userId) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.userId = userId;
    }

    public LessonDTO(Long id, String name, Byte[] content, Long userId, AttendanceResponseDTO attendanceResponseDTO) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.userId = userId;
        this.attendanceResponseDTO = attendanceResponseDTO;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte[] getContent() {
        return content;
    }

    public void setContent(Byte[] content) {
        this.content = content;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public AttendanceResponseDTO getAttendanceResponseDTO() {
        return attendanceResponseDTO;
    }

    public void setAttendanceResponseDTO(AttendanceResponseDTO attendanceResponseDTO) {
        this.attendanceResponseDTO = attendanceResponseDTO;
    }

}
