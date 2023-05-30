package com.nexttech.coursemanagement.DTOs;

import org.springframework.hateoas.RepresentationModel;

import java.util.List;

public class CurriculumResponseDTO extends RepresentationModel<CurriculumResponseDTO> {
    Long courseId;
    String courseName;
    List<LessonDTO> lessonDTOList;

    public CurriculumResponseDTO(Long courseId, String courseName, List<LessonDTO> lessonDTOList) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.lessonDTOList = lessonDTOList;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public List<LessonDTO> getLessonDTOList() {
        return lessonDTOList;
    }

    public void setLessonDTOList(List<LessonDTO> lessonDTOList) {
        this.lessonDTOList = lessonDTOList;
    }
}
