package com.example.smart_schedule.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    String userName;
    String email;
    String password;
}
