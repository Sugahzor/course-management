package com.nexttech.coursemanagement.services;

import com.nexttech.coursemanagement.DTOs.LessonDTO;
import com.nexttech.coursemanagement.DTOs.UserDTO;
import com.nexttech.coursemanagement.DTOs.UserEnrollDTO;
import com.nexttech.coursemanagement.DTOs.UserLoginDTO;
import com.nexttech.coursemanagement.mappers.UserMapper;
import com.nexttech.coursemanagement.models.Course;
import com.nexttech.coursemanagement.models.User;
import com.nexttech.coursemanagement.repositories.UserRepo;
import com.nexttech.coursemanagement.util.BadRequestException;
import com.nexttech.coursemanagement.util.MyResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CourseService courseService;
    @Autowired @Lazy
    private CurriculumService curriculumService;
    @Autowired @Lazy
    private AttendanceService attendanceService;

    public void register(User user) throws BadRequestException{
        try {
            Assert.isNull(userRepo.findByUserEmail(user.getUserEmail()), "User already exists");
            userRepo.save(user);
        }
        catch(IllegalArgumentException exception) {
            throw new BadRequestException(exception.getMessage());
        }
    }

    public UserDTO login(UserLoginDTO userLoginDTO) throws BadRequestException{
        try {
            User loginUser = userRepo.findByUserEmail(userLoginDTO.getUserEmail()); //shouldn't this throw IllegalArgumentException??
            Assert.notNull(loginUser, "Email not found.");
            if (userLoginDTO.getUserPassword().equals(loginUser.getUserPassword())) {
                System.out.println("User login successful");
                return userMapper.toDto(loginUser);
            } else {
                throw new BadRequestException("Please check password.");
            }
        }
        catch(IllegalArgumentException exception) {
            System.out.println(exception.getMessage() + "IllegalArgumentException service");
            throw new BadRequestException(exception.getLocalizedMessage());
        }
        catch(BadRequestException exception) {
            throw new BadRequestException("Please check password.");
        }
    }

    public void enrollUser(UserEnrollDTO userEnrollDTO) throws BadRequestException{
        try {
            Course course = courseService.getCourse(userEnrollDTO.courseId);
            Optional<User> user = userRepo.findById(userEnrollDTO.userId);
            Assert.isTrue(user.isPresent(), "User not found.");
            Assert.notNull(course, "Course not found.");
            if (user.get().getCourses().contains(course)) {
                System.out.println("User is already enrolled in this course");
                return;
            }
            user.get().enrollToCourse(course);
            //create user's attendance for each lesson in the course
            List<LessonDTO> lessonDTOList = curriculumService.getLessonsByCourseId(course.getId());
            lessonDTOList.forEach(lesson -> attendanceService.addAttendance(user.get().getId(), course.getId(), lesson.getId()));
        }
        catch(IllegalArgumentException exception) {
            System.out.println(exception.getMessage() + "IllegalArgumentException service");
            throw new BadRequestException(exception.getLocalizedMessage());
        }
    }

    public void disenrollFromCourse(UserEnrollDTO userEnrollDTO) throws BadRequestException{
        try {
            Optional<User> user = userRepo.findById(userEnrollDTO.userId);
            Course course = courseService.getCourse(userEnrollDTO.courseId);
            Assert.isTrue(user.isPresent(), "User not found.");
            Assert.notNull(course, "Course not found.");
            attendanceService.removeAttendances(userEnrollDTO.courseId, userEnrollDTO.userId);
            //TODO: remove all homeworks
            user.get().disenrollFromCourse(course);
        }
        catch(IllegalArgumentException exception) {
            System.out.println(exception.getMessage() + "IllegalArgumentException service");
            throw new BadRequestException(exception.getLocalizedMessage());
        }
    }

    public List<User> findUsers(Optional<String> role) {
        if(role.isPresent()) {
            return userRepo.findUsersByRole(role.get());
        } else {
            List<User> userList = new ArrayList<>();
            //TODO: Warning:(90, 48) Lambda can be replaced with method reference
            userRepo.findAll().forEach(user -> userList.add(user));
            return userList;
        }
    }

    public User getUserById(Long id) {
        try {
            Assert.notNull(id, "Id cannot be null");
            Optional<User> user = userRepo.findById(id);
            Assert.isTrue(user.isPresent(), "User not found.");
            return user.get();
        }
        catch(IllegalArgumentException exception) {
            System.out.println("IllegalArgumentException caught ok");
            throw exception;
        }
        catch(NoSuchElementException exception) {
            System.out.println("NoSuchElementException catch: " + exception.getMessage());
            throw exception;
        }
    }

    public void deleteUser(Long id) {
        try {
            userRepo.deleteById(id);
        }
        catch(IllegalArgumentException exception) {
            System.out.println("IllegalArgumentException caught ok");
            throw exception;
        }
        catch(EmptyResultDataAccessException exception){
            throw new MyResourceNotFoundException("User not found");
        }
    }

    public User getUserByUserEmail(final String userEmail) {
        try {
            return userRepo.findByUserEmail(userEmail);
        }
        catch(IllegalArgumentException exception) {
            System.out.println("IllegalArgumentException caught ok");
            throw exception;
        }
        catch(EmptyResultDataAccessException exception){
            throw new MyResourceNotFoundException("User not found");
        }
    }
}
