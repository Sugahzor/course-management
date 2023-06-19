package com.nexttech.coursemanagement.DTOs;

public class UserChangePasswordDTO {
    Long userid;
    String newUserPassword;

    public UserChangePasswordDTO(Long userId, String newPassword) {
        this.userid = userId;
        this.newUserPassword = newPassword;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getNewUserPassword() {
        return newUserPassword;
    }

    public void setNewUserPassword(String newUserPassword) {
        this.newUserPassword = newUserPassword;
    }
}
