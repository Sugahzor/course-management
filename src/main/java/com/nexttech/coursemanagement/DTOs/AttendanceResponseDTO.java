package com.nexttech.coursemanagement.DTOs;

import org.springframework.hateoas.RepresentationModel;

public class AttendanceResponseDTO extends RepresentationModel<AttendanceResponseDTO> {
    Long id;
    Byte attendanceGrade;

    public AttendanceResponseDTO(Long id, Byte grade) {
        this.id = id;
        this.attendanceGrade = grade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getAttendanceGrade() {
        return attendanceGrade;
    }

    public void setAttendanceGrade(Byte attendanceGrade) {
        this.attendanceGrade = attendanceGrade;
    }
}