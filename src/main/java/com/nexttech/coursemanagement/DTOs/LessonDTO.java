package com.nexttech.coursemanagement.DTOs;

import org.springframework.hateoas.RepresentationModel;

public class LessonDTO extends RepresentationModel<LessonDTO> {
    Long id;
    String name;
    Byte[] content;
    Long userId;

    public LessonDTO(Long id, String name, Byte[] content, Long userId) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.userId = userId;
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

}
