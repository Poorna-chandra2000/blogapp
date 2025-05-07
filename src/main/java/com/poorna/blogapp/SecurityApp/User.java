package com.poorna.blogapp.SecurityApp;

import com.poorna.blogapp.SecurityApp.SecurityEnums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;

import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Table(name = "users")
//look down for Userdetails Interface at last
public class User implements UserDetails  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)

    private String email;


    private String password;
    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;


//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of();
//    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }


    //now for role based authentication you must implement remaining methods provided by Userdetails
    //also convert enums to simple granted authority
    //even without this you can do it by Mnetioning Role.getName() or ROLE_USER in security config
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
//        roles.forEach(
//                role -> {
//                    Set<SimpleGrantedAuthority> permissions = PermissionMapping.getAuthoritiesForRole(role);
//                    authorities.addAll(permissions);
//                    authorities.add(new SimpleGrantedAuthority("ROLE_"+role.name()));
//                }
//        );
//        return authorities;
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> (GrantedAuthority) () -> "ROLE_" + role.name())
                .toList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}


////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by FernFlower decompiler)
////
//
//package org.springframework.security.core.userdetails;
//
//import java.io.Serializable;
//import java.util.Collection;
//import org.springframework.security.core.GrantedAuthority;
//
//public interface UserDetails extends Serializable {
//    Collection<? extends GrantedAuthority> getAuthorities();
//
//    String getPassword();
//
//    String getUsername();
//
//    default boolean isAccountNonExpired() {
//        return true;
//    }
//
//    default boolean isAccountNonLocked() {
//        return true;
//    }
//
//    default boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    default boolean isEnabled() {
//        return true;
//    }
//}

