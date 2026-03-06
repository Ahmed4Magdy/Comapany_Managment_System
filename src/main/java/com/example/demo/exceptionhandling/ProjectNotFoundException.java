package com.example.demo.exceptionhandling;

public class ProjectNotFoundException  extends RuntimeException{

    public ProjectNotFoundException(String message){
        super(message);
    }

}
