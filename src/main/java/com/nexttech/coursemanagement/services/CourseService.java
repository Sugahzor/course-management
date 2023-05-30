package com.nexttech.coursemanagement.services;

import com.nexttech.coursemanagement.DTOs.CourseDTO;
import com.nexttech.coursemanagement.mappers.CourseMapper;
import com.nexttech.coursemanagement.models.Course;
import com.nexttech.coursemanagement.models.User;
import com.nexttech.coursemanagement.repositories.CourseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    @Autowired
    private CourseRepo courseRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private CourseMapper courseMapper;

    public CourseDTO addCourse(String name, Long userId) {
        Course existingCourse = courseRepo.findByCourseName(name);
        User existingUser = userService.getUserById(userId);
        if (existingCourse == null && existingUser != null) {
            Course newCourse = new Course(name, existingUser);
            courseRepo.save(newCourse);
            return courseMapper.toDto(newCourse);
        } else {
            System.out.println("Course already exists.");
            return null;
        }
    }

//    public CourseDTO addLessonsToCourse(CurriculumCreationDTO curriculumCreationDTO) {
//        List<Lesson> lessonList = new ArrayList<>();
//        List<LessonDTO> lessonDTOList = new ArrayList<>();
//        CourseDTO addLessonsToCourseResponse = new CourseDTO();
//
//        curriculumCreationDTO.getLessonIdList().forEach(id -> {
//            Optional<Lesson> lesson = lessonRepo.findById(id);
//            if (lesson.isPresent()) {
//                LessonDTO lessonDto = lessonMapper.toDto(lesson.get());
//                lessonList.add(lesson.get());
//                lessonDTOList.add(lessonDto);
//            } else {
//                //TODO: create lesson and then add it to course -> done by FE
//            }
//        });
//        Course course = courseRepo.findById(curriculumCreationDTO.getCourseId()).get();
//        if (course != null) {
//            //TODO: save lessons to course...? or in curriculum table?
////            -> add to curriculum
////            course.setLessons(lessonList);
//            addLessonsToCourseResponse = new CourseDTO(course.getId(),course.getCourseName(), course.getUser().getId(), lessonDTOList);
//        }
//        return addLessonsToCourseResponse;
//    }

    public Course getCourse(Long id) {
        //TODO: Warning:(64, 40) 'Optional.get()' without 'isPresent()' check
        return courseRepo.findById(id).get();
    }

    public List<CourseDTO> getCourses() {
        List<CourseDTO> courseList = new ArrayList<>();
        courseRepo.findAll().forEach(course -> {
            //TODO: add lessons list
            CourseDTO courseResponse = courseMapper.toDto(course);
            courseList.add(courseResponse);
        });
        return courseList;
    }

    public void deleteCourse(Long id) {
        Optional<Course> existingCourse = courseRepo.findById(id);
        if (existingCourse.isPresent()) {
            courseRepo.delete(existingCourse.get());
        } else {
            //throw bad id error
        }

    }
}
