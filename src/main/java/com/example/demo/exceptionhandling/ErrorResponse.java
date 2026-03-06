package com.example.demo.exceptionhandling;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
public class ErrorResponse {


    private LocalDateTime timestamp;
    private int status;
    private String message;
    private String path;
    private List<String> errors; // <-- new


//    public ErrorResponse(LocalDateTime timestamp, int status, String message, String details,List<String>errors) {
//        this.timestamp = timestamp;
//        this.status = status;
//        this.message = message;
//        this.path = details;
//        this.errors=errors;
//
//    }



}
