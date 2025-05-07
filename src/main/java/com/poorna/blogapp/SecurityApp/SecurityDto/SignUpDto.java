package com.poorna.blogapp.SecurityApp.SecurityDto;


import com.poorna.blogapp.SecurityApp.SecurityEnums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDto {
    private String email;
    private String password;
    private String name;
    private Set<Role> roles;
    //private Set<Permission> permissions;
}
