package com.hospital.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
public class ExceptionResponse {

    private LocalDateTime timestamp;
    private int status;
    private String message;
    private String path;
    private Map<String,String> errors;


}

