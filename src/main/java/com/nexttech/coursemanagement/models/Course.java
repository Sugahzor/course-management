package com.nexttech.coursemanagement.models;

import javax.persistence.*;

@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String courseName;

//TODO: add this for course-> lessons
//    @ManyToMany(cascade = { CascadeType.ALL })
//    @JoinTable(
//            name = "Curriculum",
//            joinColumns = { @JoinColumn(name = "course_id") },
//            inverseJoinColumns = { @JoinColumn(name = "lesson_id") }
//    )

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Course() {}

    public Course(String courseName, User user) {
        this.courseName = courseName;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
