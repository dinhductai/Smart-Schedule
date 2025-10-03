package com.example.smart_schedule.exception.handle;

public class DataAccessException extends RuntimeException{
    public DataAccessException(String message){
        super(message);
    }
}
