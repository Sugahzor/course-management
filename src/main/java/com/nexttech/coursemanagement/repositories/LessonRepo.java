package com.nexttech.coursemanagement.repositories;

import com.nexttech.coursemanagement.models.Lesson;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepo extends CrudRepository<Lesson, Long> {
//    Course findByLessonName(String courseName);
}
