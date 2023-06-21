package com.nexttech.coursemanagement.DTOs;

public class UserChangePasswordDTO {
    Long userid;
    String oldUserPassword;
    String newUserPassword;

    public UserChangePasswordDTO(Long userId, String oldUserPassword, String newPassword) {
        this.userid = userId;
        this.oldUserPassword = oldUserPassword;
        this.newUserPassword = newPassword;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getOldUserPassword() {
        return oldUserPassword;
    }

    public void setOldUserPassword(String oldUserPassword) {
        this.oldUserPassword = oldUserPassword;
    }

    public String getNewUserPassword() {
        return newUserPassword;
    }

    public void setNewUserPassword(String newUserPassword) {
        this.newUserPassword = newUserPassword;
    }
}
