package com.nexttech.coursemanagement.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserChangeRoleDTO {
    Long userId;
    String newRole;
}
