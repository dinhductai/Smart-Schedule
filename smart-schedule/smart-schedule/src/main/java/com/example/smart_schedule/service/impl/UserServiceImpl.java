package com.example.smart_schedule.service.impl;

import com.example.smart_schedule.dto.request.RegisterRequest;
import com.example.smart_schedule.dto.response.UserResponse;
import com.example.smart_schedule.entity.User;
import com.example.smart_schedule.exception.DataAccessException;
import com.example.smart_schedule.exception.UserAlreadyExistException;
import com.example.smart_schedule.mapper.UserMapper;
import com.example.smart_schedule.repository.UserRepository;
import com.example.smart_schedule.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    @Override
    public UserResponse createUser(RegisterRequest userRequest) {
        try{
            Boolean existingUser = userRepository.existsByEmail(userRequest.getEmail());
            if(existingUser){
                throw new UserAlreadyExistException("User already exists with email: " + userRequest.getEmail());
            }
            else {
                return userMapper.toUserResponse(userRepository.save(userMapper.toCreateUser(userRequest)));
            }
        }catch (DataAccessException da){
            throw new DataAccessException("Cannot access database");
        }

    }
}
