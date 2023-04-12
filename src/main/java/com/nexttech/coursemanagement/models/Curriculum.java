package com.nexttech.coursemanagement.models;

import javax.persistence.*;

@Entity
public class Curriculum {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="lesson_id", nullable = false)
    private Lesson lesson;

    public Curriculum() {}

    public Long getId() {
        return id;
    }
}
