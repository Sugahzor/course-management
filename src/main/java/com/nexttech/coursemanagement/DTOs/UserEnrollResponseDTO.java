package com.nexttech.coursemanagement.DTOs;

public class UserEnrollResponseDTO {
    public Long userId;
    public Long courseId;
    boolean enrolled;

    public UserEnrollResponseDTO(Long userId, Long courseId, boolean enrolled) {
        this.userId = userId;
        this.courseId = courseId;
        this.enrolled = enrolled;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
