package com.nexttech.coursemanagement.repositories;

import com.nexttech.coursemanagement.models.Lesson;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepo extends CrudRepository<Lesson, Long> {
    List<Lesson> findByNameContainingIgnoreCase(String searchTerm);
    List<Lesson> findByUserId(Long id);
}
