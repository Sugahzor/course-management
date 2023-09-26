package com.nexttech.coursemanagement.repositories;

import com.nexttech.coursemanagement.models.Course;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepo extends CrudRepository<Course, Long> {
    Course findByCourseName(String courseName);
    List<Course> findByCourseNameContainingIgnoreCase(String searchTerm);
    List<Course> findByUserId(Long id);
}
