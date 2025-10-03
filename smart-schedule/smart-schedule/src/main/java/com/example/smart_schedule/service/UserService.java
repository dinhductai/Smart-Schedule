package com.example.smart_schedule.service;

import com.example.smart_schedule.dto.request.RegisterRequest;
import com.example.smart_schedule.dto.request.UserRequest;
import com.example.smart_schedule.dto.response.UserResponse;

public interface UserService {
    UserResponse createUser(RegisterRequest userRequest);
}
