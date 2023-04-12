package com.nexttech.coursemanagement.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String userName;
    String userEmail;
    String userPassword;
    String userRole;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "Enrollment",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "course_id") }
    )
    private Set<Course> courses = new HashSet<>();

    public User() {}

    public User(String userName, String userEmail, String userPassword, String userRole) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.userPassword = userPassword;
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

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
//    public List<Course> getCoursesPerUser() {
//        return coursesPerUser;
//    }
//
//    public void setCoursesPerUser(List<Course> coursesPerUser) {
//        this.coursesPerUser = coursesPerUser;
//    }

}
