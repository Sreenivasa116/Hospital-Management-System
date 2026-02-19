package com.hospital.exception;

public class DoctorNotAvailableException extends RuntimeException{
    public DoctorNotAvailableException(String message) {
        super(message);
    }
}
