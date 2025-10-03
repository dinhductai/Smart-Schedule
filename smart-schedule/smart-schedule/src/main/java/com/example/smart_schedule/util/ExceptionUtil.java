package com.example.smart_schedule.util;

import com.example.smart_schedule.dto.response.ErrorDetailResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

public class ExceptionUtil {

    //400
    public static ResponseEntity<ErrorDetailResponse> handleBadRequestException(String desc, WebRequest request) {
        return new ResponseEntity<>(ErrorDetailResponse.builder()
                .timestamp(LocalDateTime.now())
                .message(desc)
                .details(request.getDescription(false))
                .build()
                , HttpStatus.BAD_REQUEST);
    }

    //401
    public static ResponseEntity<ErrorDetailResponse> handleUnauthorizedException(String desc, WebRequest request) {
        return new ResponseEntity<>(ErrorDetailResponse.builder()
                .timestamp(LocalDateTime.now())
                .message(desc)
                .details(request.getDescription(false))
                .build()
                , HttpStatus.UNAUTHORIZED);
    }

    //403
    public static ResponseEntity<ErrorDetailResponse> handleForbiddenException(String desc, WebRequest request) {
        return new ResponseEntity<>(ErrorDetailResponse.builder()
                .timestamp(LocalDateTime.now())
                .message(desc)
                .details(request.getDescription(false))
                .build()
                , HttpStatus.FORBIDDEN);
    }


    //404
    public static ResponseEntity<ErrorDetailResponse> handleNotFoundException(String desc, WebRequest request) {
        return new ResponseEntity<>(ErrorDetailResponse.builder()
                .timestamp(LocalDateTime.now())
                .message(desc)
                .details(request.getDescription(false))
                .build()
                , HttpStatus.NOT_FOUND);
    }

    //405
    public static ResponseEntity<ErrorDetailResponse> handleMethodNotAllowedException(String desc, WebRequest request) {
        return new ResponseEntity<>(ErrorDetailResponse.builder()
                .timestamp(LocalDateTime.now())
                .message(desc)
                .details(request.getDescription(false))
                .build()
                , HttpStatus.METHOD_NOT_ALLOWED);
    }

    //409
    public static ResponseEntity<ErrorDetailResponse> handleConflictException(String desc, WebRequest request) {
        return new ResponseEntity<>(ErrorDetailResponse.builder()
                .timestamp(LocalDateTime.now())
                .message(desc)
                .details(request.getDescription(false))
                .build()
                , HttpStatus.CONFLICT);
    }


    //500
    public static ResponseEntity<ErrorDetailResponse> handleInternalServerErrorException(String desc, WebRequest request) {
        return new ResponseEntity<>(ErrorDetailResponse.builder()
                .timestamp(LocalDateTime.now())
                .message(desc)
                .details(request.getDescription(false))
                .build()
                , HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //502
    public static ResponseEntity<ErrorDetailResponse> handleBadGatewayException(String desc, WebRequest request) {
        return new ResponseEntity<>(ErrorDetailResponse.builder()
                .timestamp(LocalDateTime.now())
                .message(desc)
                .details(request.getDescription(false))
                .build()
                , HttpStatus.BAD_GATEWAY);
    }

    //503
    public static ResponseEntity<ErrorDetailResponse> handleServiceUnavailableException(String desc, WebRequest request) {
        return new ResponseEntity<>(ErrorDetailResponse.builder()
                .timestamp(LocalDateTime.now())
                .message(desc)
                .details(request.getDescription(false))
                .build()
                , HttpStatus.SERVICE_UNAVAILABLE);
    }

    //504
    public static ResponseEntity<ErrorDetailResponse> handleGatewayTimeoutException(String desc, WebRequest request) {
        return new ResponseEntity<>(ErrorDetailResponse.builder()
                .timestamp(LocalDateTime.now())
                .message(desc)
                .details(request.getDescription(false))
                .build()
                , HttpStatus.GATEWAY_TIMEOUT);
    }

}
