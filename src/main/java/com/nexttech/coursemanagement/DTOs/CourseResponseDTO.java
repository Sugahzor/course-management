package com.nexttech.coursemanagement.DTOs;

import org.springframework.hateoas.RepresentationModel;

public class CourseResponseDTO extends RepresentationModel<CourseResponseDTO> {
    Long id;

    public CourseResponseDTO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
