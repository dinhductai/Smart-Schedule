package com.example.smart_schedule.exception.handle;

public class PermissionAlreadyExistException extends RuntimeException{
    public PermissionAlreadyExistException(String message){
        super(message);
    }
}
