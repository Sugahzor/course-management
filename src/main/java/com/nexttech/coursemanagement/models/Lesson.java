package com.nexttech.coursemanagement.models;

import javax.persistence.*;

@Entity
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String name;
    Byte[] content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    public Lesson() {}

    public Lesson(String name, Byte[] content, User user) {

        this.name = name;
        this.content = content;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String lessonName) {
        this.name = lessonName;
    }

    public Byte[] getContent() {
        return content;
    }

    public void setContent(Byte[] lessonContent) {
        this.content = lessonContent;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
