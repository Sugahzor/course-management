package com.nexttech.coursemanagement.controllers;

import com.nexttech.coursemanagement.DTOs.LoginRequestDTO;
import com.nexttech.coursemanagement.DTOs.LoginResponseDTO;
import com.nexttech.coursemanagement.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("login")
    public ResponseEntity<LoginResponseDTO> doLogin(@RequestBody LoginRequestDTO request) {
        return ResponseEntity
                .ok()
                .body(authService.doLogin(request));
    }
}
