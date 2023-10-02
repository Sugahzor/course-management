package com.nexttech.coursemanagement.services;

import com.nexttech.coursemanagement.DTOs.LoginRequestDTO;
import com.nexttech.coursemanagement.models.AppUserPrincipal;
import com.nexttech.coursemanagement.DTOs.LoginResponseDTO;
import com.nexttech.coursemanagement.security.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j //lombok logger
@AllArgsConstructor
public class AuthService {
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    @Transactional
    public LoginResponseDTO doLogin(LoginRequestDTO authRequest) {
        final Authentication authentication;

        try {
            authentication =
                    authManager.authenticate(
                            new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getUserPassword())
                    );
        } catch (BadCredentialsException | LockedException ex) {
            log.error(ex.getMessage());
            throw ex;
        }

        final var user = (AppUserPrincipal) authentication.getPrincipal();
        final var jwt = jwtUtil.generateAccessToken(user);
        final var expiration = jwtUtil.getTime(jwt);
        return new LoginResponseDTO(jwt, expiration);
    }

//    @Transactional
//    public void doLogout(String jwt) {
//
//    }

}
