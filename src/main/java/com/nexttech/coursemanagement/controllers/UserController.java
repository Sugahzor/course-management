package com.nexttech.coursemanagement.controllers;

import com.nexttech.coursemanagement.DTOs.*;
import com.nexttech.coursemanagement.mappers.UserMapper;
import com.nexttech.coursemanagement.models.AppUserPrincipal;
import com.nexttech.coursemanagement.services.UserService;
import com.nexttech.coursemanagement.util.BadRequestException;
import com.nexttech.coursemanagement.util.MyResourceNotFoundException;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public UserDTO register(@RequestBody RegisterUserRequestDTO request) {
        return this.userService.registerUser(request);
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

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable("id") Long id) {
        //TODO: add to security antMatchers
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

    @PutMapping(value = "/change/role")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDTO> updateRole(@RequestBody UserChangeRoleDTO request, @Parameter(hidden = true) @AuthenticationPrincipal AppUserPrincipal principal) {
        return ResponseEntity
                .ok()
                .body(userService.updateRole(request));
    }
}
