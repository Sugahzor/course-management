package com.nexttech.coursemanagement.DTOs;

import java.util.List;

public class CurriculumCreationDTO {
    Long courseId;
    List<Long> lessonIdList;

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public List<Long> getLessonIdList() {
        return lessonIdList;
    }

    public void setLessonIdList(List<Long> lessonIdList) {
        this.lessonIdList = lessonIdList;
    }
}
