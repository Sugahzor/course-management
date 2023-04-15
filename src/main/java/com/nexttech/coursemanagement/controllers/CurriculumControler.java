package com.nexttech.coursemanagement.controllers;

import com.nexttech.coursemanagement.DTOs.CurriculumDTO;
import com.nexttech.coursemanagement.services.CurriculumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/curricula")
public class CurriculumControler {

    @Autowired
    private CurriculumService curriculumService;

    @GetMapping
    @ResponseBody
    public List<CurriculumDTO> getCurricula()    {
//        return curriculumService.getCurricula().stream();
        return new ArrayList<>();
    }
}
