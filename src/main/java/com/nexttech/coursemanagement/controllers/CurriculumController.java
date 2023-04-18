package com.nexttech.coursemanagement.controllers;

import com.nexttech.coursemanagement.DTOs.CurriculumCreationDTO;
import com.nexttech.coursemanagement.DTOs.CurriculumDTO;
import com.nexttech.coursemanagement.models.Curriculum;
import com.nexttech.coursemanagement.services.CurriculumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/curricula")
public class CurriculumController {

    @Autowired
    private CurriculumService curriculumService;

    @GetMapping
    @ResponseBody
    public List<CurriculumDTO> getCurricula(@RequestParam(required = false)Optional<String> sortby)    {
        return curriculumService.getCurricula(sortby);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Curriculum addCurriculum(@RequestBody CurriculumCreationDTO curriculumCreationDTO) {
        return curriculumService.addCurriculum(curriculumCreationDTO);
    }

}
