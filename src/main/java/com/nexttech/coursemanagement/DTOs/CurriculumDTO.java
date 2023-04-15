package com.nexttech.coursemanagement.DTOs;

import org.springframework.hateoas.RepresentationModel;

public class CurriculumDTO extends RepresentationModel<CurriculumDTO> {
    Long id;
    String courseName;
    String lessonName;
}
