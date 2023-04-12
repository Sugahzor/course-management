package com.nexttech.coursemanagement.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String lessonName;
    Byte[] lessonContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    public Lesson() {}

    public Lesson(Byte[] lessonContent) {
        this.lessonContent = lessonContent;
    }

    public Long getId() {
        return id;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public Byte[] getLessonContent() {
        return lessonContent;
    }

    public void setLessonContent(Byte[] lessonContent) {
        this.lessonContent = lessonContent;
    }
}
