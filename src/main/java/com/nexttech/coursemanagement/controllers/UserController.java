package com.nexttech.coursemanagement.controllers;

import com.nexttech.coursemanagement.DTOs.*;
import com.nexttech.coursemanagement.mappers.UserMapper;
import com.nexttech.coursemanagement.services.CurriculumService;
import com.nexttech.coursemanagement.services.UserService;
import com.nexttech.coursemanagement.util.BadRequestException;
import com.nexttech.coursemanagement.util.MyResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
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

    @GetMapping
    @ResponseBody
    public List<UserDTO> getUsersByRole(@RequestParam(required=false) Optional<String> role) {
        return userService.findUsers(role)
                .stream()
                .map(mapper::toDto)
                .collect(toList());
    }

    @GetMapping("/{id}")
    @ResponseBody
    public UserDTO getById(@PathVariable("id") @Validated String id) throws BadRequestException {
        try {
            return mapper.toDto(userService.getUserById(Long.parseLong(id)));
        }
        catch (IllegalArgumentException exception) {
            throw new BadRequestException("Bad request - check that id is valid: " + exception.getMessage());
        }
        catch(NoSuchElementException exception) {
            //TODO: check sending custom error messages
            // https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc
            throw new MyResourceNotFoundException("User not found - custom message not sent in response.");
        }
    }

    @PostMapping(value = "/login")
    // /session/new?
    @ResponseStatus(HttpStatus.OK)
    public void login(@RequestBody UserLoginDTO userLoginDTO) {
        try {
            userService.login(userLoginDTO);
        }
        catch(IllegalArgumentException exception) {
            System.out.println(exception.getLocalizedMessage() + "IllegalArgumentException controller");
            throw new MyResourceNotFoundException("User not found - please register or check email");
        }
        catch(BadRequestException exception) {
            System.out.println(exception.getMessage() + "BadRequestException in controlelr");
            throw new MyResourceNotFoundException(exception.getMessage());
        }

    }

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public void register(@RequestBody UserCreationDTO userCreationDto) throws BadRequestException{
        try {
            userService.register(mapper.toUser(userCreationDto));
        }
        catch(BadRequestException exception) {
            System.out.println(exception.getMessage() + "controller - custom messages not working....");
            throw new BadRequestException(exception.getMessage());
        }
    }

    @PostMapping(value = "/enroll")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public void enroll(@RequestBody UserEnrollDTO userEnrollDTO) {
        userService.enrollUser(userEnrollDTO);
    }

    @PostMapping(value = "/disenroll")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public void disenrollFromCourse(@RequestBody UserEnrollDTO userEnrollDTO) {
        userService.disenrollFromCourse(userEnrollDTO);
    }

    @GetMapping(value = "/curricula/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<CurriculaResponseDTO> getUserCurricula(@PathVariable("id") Long userId) {
        return curriculumService.getCurriculaByUser(userId);
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
}
