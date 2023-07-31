package com.nexttech.coursemanagement.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {
    private String userName;
    private String userPassword;
}
