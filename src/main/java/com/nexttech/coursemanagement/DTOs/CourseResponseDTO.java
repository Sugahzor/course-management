package com.nexttech.coursemanagement.DTOs;

import org.springframework.hateoas.RepresentationModel;

public class CourseResponseDTO extends RepresentationModel<CourseResponseDTO> {
    Long id;
    UserDTO user;

    public CourseResponseDTO(Long id, UserDTO user) {
        this.id = id;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
