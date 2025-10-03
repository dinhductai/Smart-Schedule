package com.example.smart_schedule.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IntrospectRequest {
    String token;

    public IntrospectRequest(String token) {
        this.token = token;
    }
}