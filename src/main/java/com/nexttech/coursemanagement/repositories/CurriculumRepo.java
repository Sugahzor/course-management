package com.nexttech.coursemanagement.repositories;

import com.nexttech.coursemanagement.models.Curriculum;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurriculumRepo extends CrudRepository<Curriculum, Long> {

    //TODO: arent they all distinct anyway? check and refactor
    List<Curriculum> findAllDistinctByCourse_Id(Long courseId);
    List<Curriculum> findAllByCourse_Id(Long courseId);
    List<Curriculum> findAllByLessonId(Long lessonId);
    Curriculum findByCourse_IdAndLesson_Id(Long courseId, Long lessonId);
}
