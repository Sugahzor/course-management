package com.nexttech.coursemanagement.DTOs;

public class UserEnrollResponseDTO {
    public Long courseId;
    boolean enrolled;

    public UserEnrollResponseDTO(Long courseId, boolean enrolled) {
        this.courseId = courseId;
        this.enrolled = enrolled;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public boolean isEnrolled() {
        return enrolled;
    }

    public void setEnrolled(boolean enrolled) {
        this.enrolled = enrolled;
    }

}
