package com.example.smart_schedule.dto.response;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetailResponse {
    private LocalDateTime timestamp;
    private String message;
    private String details;
}