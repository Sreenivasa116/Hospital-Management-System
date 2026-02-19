package com.hospital.exception;



import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CustomMsg {

    private static final Map<String, String> FIELD_MESSAGES = Map.of(
            "password", "Incorrect password format,should include uppercase,lowercase and a digit",
            "phoneNumber", "Invalid Phone number",
            "address", "Invalid Address,please check again",
            "name", "Invalid Name ",
            "email", "Invalid Email Address",
            "oldPassword", "Password must contain at least 1 uppercase, 1 lowercase, 1 digit, and 1 special character.",
            "newPassword","Password must contain at least 1 uppercase, 1 lowercase, 1 digit, and 1 special character."
    );
    public String getMessage(String field,String defaultMessage){
        return FIELD_MESSAGES.getOrDefault(field,defaultMessage);
    }
}
