package com.example.demo.exceptionhandling;

public class EmployeeHasTasksException extends RuntimeException{

    public EmployeeHasTasksException(String message){
        super(message);
    }

}
