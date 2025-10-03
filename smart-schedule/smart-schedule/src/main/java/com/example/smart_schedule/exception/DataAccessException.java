package com.example.smart_schedule.exception;

public class DataAccessException extends RuntimeException{
    public DataAccessException(String message){
        super(message);
    }
}
