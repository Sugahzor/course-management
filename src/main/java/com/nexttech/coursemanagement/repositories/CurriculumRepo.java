package com.nexttech.coursemanagement.repositories;

import com.nexttech.coursemanagement.models.Curriculum;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurriculumRepo extends CrudRepository<Curriculum, Long> {

    List<Curriculum> findAllDistinctByCourse_Id(Long courseId);

}
