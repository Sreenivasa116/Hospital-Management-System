package com.hospital.exception;

public class PatientAlreadyExistsByEmail extends RuntimeException{
    public PatientAlreadyExistsByEmail(String message) {
        super(message);
    }
}
