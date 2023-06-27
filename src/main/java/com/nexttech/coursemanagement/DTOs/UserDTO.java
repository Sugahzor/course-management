package com.nexttech.coursemanagement.DTOs;

import org.springframework.hateoas.RepresentationModel;

import java.util.List;

public class UserDTO extends RepresentationModel<UserDTO> {
    Long id;
    String userName;
    String userEmail;
    String userRole;
    List<Long> coursesEnrolledTo;

    public UserDTO(Long id, String userName, String userEmail, String userRole, List<Long> coursesEnrolledTo) {
        this.id = id;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userRole = userRole;
        this.coursesEnrolledTo = coursesEnrolledTo;
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

    public List<Long> getCoursesEnrolledTo() {
        return coursesEnrolledTo;
    }

    public void setCoursesEnrolledTo(List<Long> coursesEnrolledTo) {
        this.coursesEnrolledTo = coursesEnrolledTo;
    }
}
