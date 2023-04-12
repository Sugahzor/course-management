package com.nexttech.coursemanagement.DTOs;

public class UserLoginDTO {
    String userEmail;
    String userPassword;

    public UserLoginDTO(String userEmail, String userPassword) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }
}
