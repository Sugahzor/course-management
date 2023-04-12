package com.nexttech.coursemanagement.DTOs;

public class UserDTO {
    Long id;
    String userName;
    String userEmail;
    String userRole;

    public UserDTO(Long id, String userName, String userEmail, String userRole) {
        this.id = id;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userRole = userRole;
    }

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
