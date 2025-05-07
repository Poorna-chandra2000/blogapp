package com.poorna.blogapp.SecurityApp.SecurityService;


import com.poorna.blogapp.SecurityApp.SecurityDto.SignUpDto;
import com.poorna.blogapp.SecurityApp.SecurityDto.UserDto;
import com.poorna.blogapp.SecurityApp.SecuriyRepository.UserRepository;
import com.poorna.blogapp.SecurityApp.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    /// for both Jwt and Admin Dashboard ^

    /// Overriding only happens when parent has same method as child so child must extend it or implents i.e inheritence or interfaces
    @Override //this abstract method comes from UserDetailService Interface which is implemented above
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //this method enables you by what you must login along with password i.e email or username
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new BadCredentialsException("User with email "+ username +" not found"));
    }

    //used for custom jwt filter as Id is important to validate
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User with id "+ userId +
                " not found"));
    }

    public User getUsrByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
    /// for both Jwt and Admin Dashboard ^

   /// Sighnup
    public UserDto signUp(SignUpDto signUpDto) {
        Optional<User> user = userRepository.findByEmail(signUpDto.getEmail());
        if(user.isPresent()) {
            throw new BadCredentialsException("User with email already exits "+ signUpDto.getEmail());
        }

        User toBeCreatedUser = modelMapper.map(signUpDto, User.class);


        toBeCreatedUser.setPassword(passwordEncoder.encode(toBeCreatedUser.getPassword()));

        User savedUser = userRepository.save(toBeCreatedUser);


        return modelMapper.map(savedUser, UserDto.class);
    }

    //alternate without model mapper sometimes roles dont get mapped
//public UserDto signUp(SignUpDto signUpDto) {
//    Optional<User> user = userRepository.findByEmail(signUpDto.getEmail());
//    if (user.isPresent()) {
//        throw new BadCredentialsException("User with email already exists " + signUpDto.getEmail());
//    }
//
//    User toBeCreatedUser = User.builder()
//            .email(signUpDto.getEmail())
//            .password(passwordEncoder.encode(signUpDto.getPassword()))
//            .name(signUpDto.getName())
//            .roles(signUpDto.getRoles())
//            .build();
//
//    User savedUser = userRepository.save(toBeCreatedUser);
//
//    // Also manually map User to UserDto
//    UserDto userDto = new UserDto();
//    userDto.setId(savedUser.getId());
//    userDto.setEmail(savedUser.getEmail());
//    userDto.setName(savedUser.getName());
//   // userDto.setRoles(savedUser.getRoles());
//
//    return userDto;
//}

    /// for both Oauth ^ because it provides secured token by itself
    public User save(User newUser) {
        return userRepository.save(newUser);
    }
}






















