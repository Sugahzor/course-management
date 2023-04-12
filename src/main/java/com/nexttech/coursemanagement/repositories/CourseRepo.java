package com.nexttech.coursemanagement.repositories;

import com.nexttech.coursemanagement.models.Course;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepo extends CrudRepository<Course, Long> {
    Course findByCourseName(String courseName);
}
