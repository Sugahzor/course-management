package com.nexttech.coursemanagement.controllers;

import com.nexttech.coursemanagement.DTOs.UserCreationDTO;
import com.nexttech.coursemanagement.DTOs.UserDTO;
import com.nexttech.coursemanagement.DTOs.UserLoginDTO;
import com.nexttech.coursemanagement.mappers.UserMapper;
import com.nexttech.coursemanagement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    //TODO: interpret exceptions here
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper mapper;

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
    public UserDTO getById(@PathVariable("id") @Validated Long id) {
        return mapper.toDto(userService.getUserById(id));
    }

    @PostMapping(value = "/login")
    // /session/new?
    @ResponseStatus(HttpStatus.OK)
    public void login(@RequestBody UserLoginDTO userLoginDTO) {
        userService.login(userLoginDTO);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public void register(@RequestBody UserCreationDTO userCreationDto) {
        userService.register(mapper.toUser(userCreationDto));
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable("id") Long id) throws Exception {
        //handle thrown errors by the service
        userService.deleteUser(id);
    }
}
