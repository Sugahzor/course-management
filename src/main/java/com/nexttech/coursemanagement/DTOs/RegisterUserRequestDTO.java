package com.nexttech.coursemanagement.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserRequestDTO {
    String userName;
    String userEmail;
    String userPassword;
}
