package com.example.smart_schedule.exception.handle;

public class ExpiredJwtException extends RuntimeException{
    public ExpiredJwtException(String message){
        super(message);
    }
}
