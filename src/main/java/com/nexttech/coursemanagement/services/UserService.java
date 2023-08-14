package com.nexttech.coursemanagement.services;

import com.nexttech.coursemanagement.DTOs.*;
import com.nexttech.coursemanagement.mappers.UserMapper;
import com.nexttech.coursemanagement.models.Course;
import com.nexttech.coursemanagement.models.User;
import com.nexttech.coursemanagement.repositories.UserRepo;
import com.nexttech.coursemanagement.util.BadRequestException;
import com.nexttech.coursemanagement.util.MyResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;

@Service
@Slf4j
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDTO registerUser(RegisterUserRequestDTO request) {
        if (this.userRepo.findByUserName(request.getUserName()).isPresent()) {
            throw new RuntimeException("Username taken.");
        }
        // TODO: userRole set to default by the mapper - maybe not the best approach; refactor if needed
        User user = userMapper.toUser(request);
        user.setUserPassword(passwordEncoder.encode(request.getUserPassword()));

        //TODO: our model only needs 1 role / user
        // default role for new users
//        RoleEntity role = roleRepository
//                .findById(RoleConstants.USER)
//                .orElseThrow();
//        userEntity.setRoles(new HashSet<>(Collections.singleton(role)));
        userRepo.save(user);
        return userMapper.toDto(user);
    }

    public UserEnrollResponseDTO enrollUser(String username, Long courseId) throws BadRequestException{
        try {
            Course course = courseService.getCourse(courseId);
            Optional<User> user = userRepo.findByUserName(username);
            Assert.isTrue(user.isPresent(), "User not found.");
            Assert.notNull(course, "Course not found.");
            if (user.get().getCourses().contains(course)) {
                throw new BadRequestException("User is already enrolled in this course");
            }
            user.get().enrollToCourse(course);
            //create user's attendance for each lesson in the course
            List<LessonDTO> lessonDTOList = curriculumService.getLessonsByCourseId(course.getId());
            lessonDTOList.forEach(lesson -> attendanceService.addAttendance(user.get().getId(), course.getId(), lesson.getId()));
            return new UserEnrollResponseDTO(course.getId(), true);
        }
        catch(IllegalArgumentException exception) {
            System.out.println(exception.getMessage() + "IllegalArgumentException service");
            throw new BadRequestException(exception.getLocalizedMessage());
        }
        catch(BadRequestException exception) {
            throw new BadRequestException(exception.getMessage());
        }
    }

    public UserEnrollResponseDTO disenrollFromCourse(String username, Long courseId) throws BadRequestException{
        try {
            Optional<User> user = userRepo.findByUserName(username);
            Course course = courseService.getCourse(courseId);
            Assert.isTrue(user.isPresent(), "User not found.");
            Assert.notNull(course, "Course not found.");
            attendanceService.removeAttendances(courseId, user.get().getId());
            //TODO: remove all homeworks
            user.get().disenrollFromCourse(course);
            return new UserEnrollResponseDTO(course.getId(), false);
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
            userList.sort((user1, user2) -> CharSequence.compare(user1.getUserRole(), user2.getUserRole()));
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
            User user = userRepo.findById(id).get();
            List<Course> toRemove = new ArrayList<>();
            user.getCourses().forEach(course -> toRemove.add(course));
            toRemove.forEach(course -> user.disenrollFromCourse(course));
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

    public UserDTO getByUsername(String username) {
        return this.userRepo.findByUserName(username)
                .map(this.userMapper::toDto)
                .orElseThrow();
    }

    public void changeUserEmail(UserChangeEmailDTO userChangeEmailDTO) {
        try {
            Optional<User> user = userRepo.findById(userChangeEmailDTO.getUserId());
            Assert.isTrue(user.isPresent(), "User not found.");
            user.get().setUserEmail(userChangeEmailDTO.getNewUserEmail());
            userRepo.save(user.get());
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

    public void changeUserPassword(UserChangePasswordDTO userChangePasswordDTO) throws BadRequestException{
        try{
            Optional<User> user = userRepo.findById(userChangePasswordDTO.getUserid());
            Assert.isTrue(user.isPresent(), "User not found.");
            if (user.get().getUserPassword().equals(userChangePasswordDTO.getOldUserPassword())) {
                user.get().setUserPassword(userChangePasswordDTO.getNewUserPassword());
                userRepo.save(user.get());
            } else {
                throw new BadRequestException("Old password doesn't match.");
            }
        }
        catch(IllegalArgumentException exception) {
            System.out.println("IllegalArgumentException caught ok");
            throw exception;
        }
        catch(NoSuchElementException exception) {
            System.out.println("NoSuchElementException catch: " + exception.getMessage());
            throw exception;
        }
        catch(BadRequestException exception) {
            throw exception;
        }
    }

    public UserDTO updateRole(UserChangeRoleDTO request) {
        try {
            User user = userRepo.findById(request.getUserId()).orElseThrow();
            user.setUserRole(request.getNewRole());
            userRepo.save(user);
            return userMapper.toDto(user);
        }
        catch (NoSuchElementException exception) {
            log.error(exception.getMessage());
            throw exception;
        }
    }
}
