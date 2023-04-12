package com.nexttech.coursemanagement.models;

import javax.persistence.*;

@Entity
public class Homework {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Byte[] homeworkContent;
    Byte homeworkGrade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="attendance_id", nullable = false)
    private Attendance attendance;

    public Homework() {}

    public Homework(Byte[] homeworkContent, Byte homeworkGrade) {
        this.homeworkContent = homeworkContent;
        this.homeworkGrade = homeworkGrade;
    }

    public Long getId() {
        return id;
    }

    public Byte[] getHomeworkContent() {
        return homeworkContent;
    }

    public void setHomeworkContent(Byte[] homeworkContent) {
        this.homeworkContent = homeworkContent;
    }

    public Byte getHomeworkGrade() {
        return homeworkGrade;
    }

    public void setHomeworkGrade(Byte homeworkGrade) {
        this.homeworkGrade = homeworkGrade;
    }
}
