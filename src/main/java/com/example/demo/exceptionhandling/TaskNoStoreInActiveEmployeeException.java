package com.example.demo.exceptionhandling;

public class TaskNoStoreInActiveEmployeeException extends RuntimeException{

    public TaskNoStoreInActiveEmployeeException(String message){
        super(message);
    }

}
