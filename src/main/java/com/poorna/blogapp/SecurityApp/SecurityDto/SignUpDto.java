package com.poorna.blogapp.SecurityApp.SecurityDto;


import com.poorna.blogapp.SecurityApp.SecurityEnums.Role;
import lombok.Data;

import java.util.Set;

@Data
public class SignUpDto {
    private String email;
    private String password;
    private String name;
    private Set<Role> roles;
    //private Set<Permission> permissions;
}
