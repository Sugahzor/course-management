package com.nexttech.coursemanagement.DTOs;

import org.springframework.hateoas.RepresentationModel;

public class AttendanceResponseDTO extends RepresentationModel<AttendanceResponseDTO> {
    Long id;
    Byte attendanceGrade;

    public AttendanceResponseDTO(Long id, Byte grade) {
        this.id = id;
        this.attendanceGrade = grade;
    }
}