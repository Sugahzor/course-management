package com.nexttech.coursemanagement.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponseDTO {
    private String jwt;
    private Date expiration;
}
