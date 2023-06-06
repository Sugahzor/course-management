package com.nexttech.coursemanagement.DTOs;

public class UserEnrollDTO {
    public Long userId;
    public Long courseId;

    public UserEnrollDTO(Long userId, Long courseId) {
        this.userId = userId;
        this.courseId = courseId;
    }
}