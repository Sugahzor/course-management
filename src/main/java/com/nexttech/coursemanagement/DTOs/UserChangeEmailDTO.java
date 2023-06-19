package com.nexttech.coursemanagement.DTOs;

public class UserChangeEmailDTO {
    Long userId;
    String newUserEmail;

    public UserChangeEmailDTO(Long userId, String newUserEmail) {
        this.userId = userId;
        this.newUserEmail = newUserEmail;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNewUserEmail() {
        return newUserEmail;
    }

    public void setNewUserEmail(String newUserEmail) {
        this.newUserEmail = newUserEmail;
    }
}
