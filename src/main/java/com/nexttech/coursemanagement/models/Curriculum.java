package com.nexttech.coursemanagement.models;

import javax.persistence.*;

@Entity
public class Curriculum {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="lesson_id", nullable = false)
    private Lesson lesson;

    public Curriculum() {}

    public Curriculum(Course course, Lesson lesson) {
        this.course = course;
        this.lesson = lesson;
    }

    public Long getId() {
        return id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }
}
