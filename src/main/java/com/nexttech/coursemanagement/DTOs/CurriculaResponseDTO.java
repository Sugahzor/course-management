package com.nexttech.coursemanagement.DTOs;

import org.springframework.hateoas.RepresentationModel;

import java.util.List;

public class CurriculaResponseDTO extends RepresentationModel<CurriculaResponseDTO> {
    Long courseId;
    String courseName;
    String imageUrl;
    List<LessonDTO> lessonDTOList;

    public CurriculaResponseDTO(Long courseId, String courseName, String imageUrl, List<LessonDTO> lessonDTOList) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.imageUrl = imageUrl;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<LessonDTO> getLessonDTOList() {
        return lessonDTOList;
    }

    public void setLessonDTOList(List<LessonDTO> lessonDTOList) {
        this.lessonDTOList = lessonDTOList;
    }
}
