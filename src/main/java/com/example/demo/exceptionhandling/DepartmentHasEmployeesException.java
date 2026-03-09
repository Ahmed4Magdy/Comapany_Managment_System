package com.example.demo.exceptionhandling;

public class DepartmentHasEmployeesException extends RuntimeException{

    public DepartmentHasEmployeesException(String message){

        super(message);

    }

}
