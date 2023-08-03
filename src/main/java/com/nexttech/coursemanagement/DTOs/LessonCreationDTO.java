package com.nexttech.coursemanagement.DTOs;

public class LessonCreationDTO {
    String name;
    Byte[] content;


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
}
