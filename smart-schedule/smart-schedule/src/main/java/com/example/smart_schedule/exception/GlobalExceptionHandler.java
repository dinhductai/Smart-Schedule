package com.example.smart_schedule.exception;

import com.example.smart_schedule.dto.response.ErrorDetailResponse;
import com.example.smart_schedule.exception.handle.*;
import com.example.smart_schedule.util.ExceptionUtil;
import com.nimbusds.jose.JOSEException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.naming.AuthenticationException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // --- Lỗi chung & Bảo mật ---

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDetailResponse> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        return buildErrorResponse(ex.getMessage(), request, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDetailResponse> handleHttpMessageNotReadableException(WebRequest request) {
        return buildErrorResponse("Invalid request body", request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorDetailResponse> handleDataAccessException(DataAccessException ex, WebRequest request) {
        return buildErrorResponse(ex.getMessage(), request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDetailResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        return buildErrorResponse(ex.getMessage(), request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorDetailResponse> handleForbiddenException(ForbiddenException ex, WebRequest request) {
        return buildErrorResponse(ex.getMessage(), request, HttpStatus.FORBIDDEN);
    }

    // --- Lỗi Xác thực (Authentication) & JWT ---

    @ExceptionHandler({AuthenticationException.class, BadCredentialsException.class})
    public ResponseEntity<ErrorDetailResponse> handleBadCredentialsException(WebRequest request) {
        return buildErrorResponse("Incorrect email or password", request, HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorDetailResponse> handleExpiredJwtException(WebRequest request) {
        return buildErrorResponse("Token has expired", request, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(TokenInvalid.class)
    public ResponseEntity<ErrorDetailResponse> handleTokenInvalid(WebRequest request) {
        return buildErrorResponse("Token invalid", request, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(GenericAuthenticationException.class)
    public ResponseEntity<ErrorDetailResponse> handleGenericAuthenticationException(GenericAuthenticationException ex, WebRequest request) {
        return buildErrorResponse(ex.getMessage(), request, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JOSEException.class)
    public ResponseEntity<ErrorDetailResponse> handleJoseException(JOSEException ex, WebRequest request) {
        return buildErrorResponse(ex.getMessage(), request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ParseException.class)
    public ResponseEntity<ErrorDetailResponse> handleParseException(ParseException ex, WebRequest request) {
        return buildErrorResponse(ex.getMessage(), request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IntrospectAuthenticationException.class)
    public ResponseEntity<ErrorDetailResponse> handleIntrospectAuthenticationException(IntrospectAuthenticationException ex, WebRequest request) {
        return buildErrorResponse(ex.getMessage(), request, HttpStatus.UNAUTHORIZED);
    }


    // --- Lỗi liên quan đến Logic nghiệp vụ (Business Logic) ---

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDetailResponse> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        return buildErrorResponse(ex.getMessage(), request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<ErrorDetailResponse> handleEmailAlreadyInUseException(EmailAlreadyInUseException ex, WebRequest request) {
        return buildErrorResponse(ex.getMessage(), request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PasswordIncorrectException.class)
    public ResponseEntity<ErrorDetailResponse> handlePasswordIncorrectException(PasswordIncorrectException ex, WebRequest request) {
        return buildErrorResponse(ex.getMessage(), request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({PermissionNotFoundException.class, RoleNotFoundException.class})
    public ResponseEntity<ErrorDetailResponse> handleResourceNotFound(Exception ex, WebRequest request) {
        return buildErrorResponse(ex.getMessage(), request, HttpStatus.NOT_FOUND);
    }



    // --- Xử lý lỗi Validation (GIỮ NGUYÊN) ---

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // --- Phương thức xây dựng Response chung ---

    private ResponseEntity<ErrorDetailResponse> buildErrorResponse(String message, WebRequest request, HttpStatus status) {
        ErrorDetailResponse errorDetail = new ErrorDetailResponse(
                LocalDateTime.now(),
                message,
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetail, status);
    }
}