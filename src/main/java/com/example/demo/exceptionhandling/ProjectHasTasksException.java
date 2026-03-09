package com.example.demo.exceptionhandling;

public class ProjectHasTasksException extends RuntimeException {

    public  ProjectHasTasksException(String message){
        super(message);
    }
}
