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
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="curriculum_id", nullable = false)
    private Curriculum curriculum;

    public Attendance() {}

    public Attendance(Byte attendanceGrade) {
        this.attendanceGrade = attendanceGrade;
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
