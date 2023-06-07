package com.nexttech.coursemanagement.models;

import javax.persistence.*;

@Entity
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Byte attendanceGrade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    //TODO: Warning:(14, 18) Private field 'user' is assigned but never accessed
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="curriculum_id", nullable = false)
    //TODO: Warning:(18, 24) Private field 'curriculum' is assigned but never accessed
    private Curriculum curriculum;

    public Attendance() {}

    public Attendance(Byte attendanceGrade) {
        this.attendanceGrade = attendanceGrade;
    }

    public Attendance(User user, Curriculum curriculum) {
        this.user = user;
        this.curriculum = curriculum;
    }

    public Long getId() {
        return id;
    }

    public Byte getAttendanceGrade() {
        return attendanceGrade;
    }

    public void setAttendanceGrade(Byte attendanceGrade) {
        this.attendanceGrade = attendanceGrade;
    }
}
