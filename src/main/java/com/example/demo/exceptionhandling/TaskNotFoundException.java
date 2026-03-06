package com.example.demo.exceptionhandling;

public class TaskNotFoundException extends RuntimeException{

     public TaskNotFoundException(String message){
         super(message);
     }

}
