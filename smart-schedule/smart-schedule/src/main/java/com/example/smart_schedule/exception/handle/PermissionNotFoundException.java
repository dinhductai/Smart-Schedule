package com.example.smart_schedule.exception.handle;

public class PermissionNotFoundException extends RuntimeException{
    public PermissionNotFoundException(String message) {
        super(message);
    }
}
