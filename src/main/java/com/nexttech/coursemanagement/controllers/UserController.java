package com.nexttech.coursemanagement.controllers;

import com.nexttech.coursemanagement.DTOs.*;
import com.nexttech.coursemanagement.mappers.UserMapper;
import com.nexttech.coursemanagement.models.AppUserPrincipal;
import com.nexttech.coursemanagement.services.CurriculumService;
import com.nexttech.coursemanagement.services.UserService;
import com.nexttech.coursemanagement.util.BadRequestException;
import com.nexttech.coursemanagement.util.MyResourceNotFoundException;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper mapper;
    @Autowired @Lazy
    private CurriculumService curriculumService;

    @PostMapping("register")
    @ResponseStatus(HttpStatus.OK)
    public void register(@RequestBody RegisterUserRequestDTO request) {
        this.userService.registerUser(request);
    }

    @GetMapping("info")
    public ResponseEntity<UserDTO> currentUserInfo(@Parameter(hidden = true) @AuthenticationPrincipal AppUserPrincipal principal) {
        return ResponseEntity
                .ok()
                .body(userService.getByUsername(principal.getUsername()));
    }

    @GetMapping(value = "/enroll/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public ResponseEntity<UserEnrollResponseDTO> enroll(@Parameter(hidden = true) @AuthenticationPrincipal AppUserPrincipal principal, @PathVariable("id") Long courseId) throws BadRequestException{
        return ResponseEntity
                .ok()
                .body(userService.enrollUser(principal.getUsername(), courseId));
    }

    @GetMapping(value = "/disenroll/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public ResponseEntity<UserEnrollResponseDTO> disenrollFromCourse(@Parameter(hidden = true) @AuthenticationPrincipal AppUserPrincipal principal, @PathVariable("id") Long courseId) throws BadRequestException{
        return ResponseEntity
                .ok()
                .body(userService.disenrollFromCourse(principal.getUsername(), courseId));
    }

    @GetMapping
    @ResponseBody
    public List<UserDTO> getUsersByRole(@RequestParam(required=false) Optional<String> role) {
        return userService.findUsers(role)
                .stream()
                .map(mapper::toDto)
                .collect(toList());
    }

    @GetMapping(value = "/{id}/curricula")
    @ResponseStatus(HttpStatus.OK)
    public List<CourseResponseDTO> getUserCurricula(@PathVariable("id") Long userId) {
        return userService.getCoursesWithLessonsByUser(userId);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable("id") Long id) {
        //TODO: should parameter id be string or Long?
        try {
            userService.deleteUser(id);
        }
        catch (EmptyResultDataAccessException exception) {
            throw new MyResourceNotFoundException("user not found, please check id");
        }
    }

    @GetMapping(value = "info/{userEmail}")
    public UserDTO getUserByUserEmail(@PathVariable final String userEmail) {
        return mapper.toDto(userService.getUserByUserEmail(userEmail));
    }

    @PutMapping(value = "/change/email")
    @ResponseStatus(HttpStatus.OK)
    public void changeUserEmail(@RequestBody UserChangeEmailDTO userChangeEmailDTO) {
        userService.changeUserEmail(userChangeEmailDTO);
    }

    @PutMapping(value = "/change/password")
    @ResponseStatus(HttpStatus.OK)
    public void changeUserPassword(@RequestBody UserChangePasswordDTO userChangePasswordDTO) throws BadRequestException{
        userService.changeUserPassword(userChangePasswordDTO);
    }
}
