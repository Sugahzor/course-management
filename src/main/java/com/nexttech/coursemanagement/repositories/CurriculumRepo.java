package com.nexttech.coursemanagement.repositories;

import com.nexttech.coursemanagement.models.Curriculum;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurriculumRepo extends CrudRepository<Curriculum, Long> {

}
