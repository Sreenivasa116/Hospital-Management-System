package com.hospital.exception;

public class DoctorAvailabilityNotFound extends RuntimeException{
    public DoctorAvailabilityNotFound(String message) {
        super(message);
    }
}
