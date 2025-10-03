package com.example.smart_schedule.mapper;

import com.example.smart_schedule.dto.request.RegisterRequest;
import com.example.smart_schedule.dto.response.UserResponse;
import com.example.smart_schedule.entity.User;
import com.example.smart_schedule.enumeration.AccountStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    public User toCreateUser(RegisterRequest registerRequest){
        String passwordEncoded = passwordEncoder.encode(registerRequest.getPassword());

        return User.builder()
                .username(registerRequest.getUserName())
                .password(passwordEncoded)
                .email(registerRequest.getEmail())
                .accountStatus(AccountStatus.ACTIVE)
                .build();
    }

    public UserResponse toUserResponse(User user){
        return UserResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .userName(user.getUsername())
                .profile(user.getProfile())
                .build();
    }
}
